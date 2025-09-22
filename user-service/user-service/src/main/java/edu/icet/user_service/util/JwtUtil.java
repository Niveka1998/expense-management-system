package edu.icet.user_service.util;

import edu.icet.user_service.model.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey jwtSecret;
    private final int jwtExpirationMs = 86400000; // 24 hours

    // If you want to configure the secret from application.properties
    public JwtUtil(@Value("${jwt.secret:}") String secretKey) {
        if (secretKey != null && !secretKey.trim().isEmpty()) {
            // Use the provided secret key (make sure it's long enough)
            this.jwtSecret = Keys.hmacShaKeyFor(secretKey.getBytes());
        } else {
            // Generate a secure key automatically (for development)
            this.jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
    }

    public String generateJwtToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getUserId())
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    public String getUsernameFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        } catch (JwtException e) {
            System.err.println("JWT error: " + e.getMessage());
        }
        return false;
    }
}