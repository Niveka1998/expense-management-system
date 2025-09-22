package edu.icet.category_service.config;

import edu.icet.category_service.client.UserClient;
import edu.icet.category_service.model.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserClient userClient;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        logger.info("=== JWT AUTHENTICATION FILTER START ===");
        logger.info("Incoming Request: {} {}", method, requestUri);
        logger.info("Remote Address: {}", request.getRemoteAddr());
        logger.info("Request Headers:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            if ("authorization".equalsIgnoreCase(headerName)) {
                logger.info("  {}: {}", headerName, "Bearer ******"); // Mask token for security
            } else {
                logger.info("  {}: {}", headerName, request.getHeader(headerName));
            }
        });

        try {
            String jwt = getJwtFromRequest(request);
            logger.info("JWT extracted from request: {}", (jwt != null ? "YES" : "NO"));

            if (jwt != null) {
                logger.info("JWT token length: {} characters", jwt.length());
                logger.info("Validating token with user service...");

                boolean isValid = validateTokenWithUserService(jwt);
                logger.info("Token validation result: {}", isValid);

                if (isValid) {
                    logger.info("Token is valid. Fetching user information...");
                    UserDTO userDTO = getUserInfoFromToken(jwt);

                    if (userDTO != null) {
                        logger.info("User authenticated successfully: {} (ID: {}, Email: {})",
                                userDTO.getUsername(), userDTO.getUserId(), userDTO.getEmail());

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDTO, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        logger.info("Authentication set in SecurityContext for user: {}", userDTO.getUsername());
                    } else {
                        logger.warn("User info is null despite valid token. This might indicate an issue with user service.");
                        sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "User information not found");
                        return;
                    }
                } else {
                    logger.warn("Token validation failed. The token may be invalid, expired, or user service is unavailable.");
                    sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid or expired authentication token");
                    return;
                }
            } else {
                logger.warn("No JWT token found in Authorization header. Request requires authentication.");
                logger.warn("Expected header: Authorization: Bearer <token>");
                sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Missing authentication token");
                return;
            }
        } catch (Exception e) {
            logger.error("Unexpected error during authentication: {}", e.getMessage(), e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication processing error: " + e.getMessage());
            return;
        }

        logger.info("=== JWT AUTHENTICATION FILTER END - REQUEST PROCEEDING ===");
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateTokenWithUserService(String token) {
        try {
            logger.debug("Calling user service to validate token...");
            boolean isValid = userClient.validateToken("Bearer " + token);
            logger.debug("User service validation response: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Token validation failed with user service: {}", e.getMessage());
            logger.error("Exception type: {}", e.getClass().getName());
            return false;
        }
    }

    private UserDTO getUserInfoFromToken(String token) {
        try {
            logger.debug("Calling user service to get user info from token...");
            UserDTO userDTO = userClient.getUserInfo("Bearer " + token);
            if (userDTO != null) {
                logger.debug("User service returned user: {}", userDTO.getUsername());
            } else {
                logger.warn("User service returned null user DTO");
            }
            return userDTO;
        } catch (Exception e) {
            logger.error("Failed to get user info from token: {}", e.getMessage());
            logger.error("Exception type: {}", e.getClass().getName());
            return null;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        logger.warn("Sending error response: {} - {}", status, message);
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\", \"status\": %d}",
                "Authentication Failed", message, status);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    // Add this method to log the configuration at startup
    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        logger.info("JwtAuthenticationFilter initialized");
        logger.info("UserClient available: {}", userClient != null ? "YES" : "NO");
    }
}