package edu.icet.user_service.service.impl;

import edu.icet.user_service.model.dto.*;
import edu.icet.user_service.model.entity.UserEntity;
import edu.icet.user_service.repository.UserRepository;
import edu.icet.user_service.service.AuthService;
import edu.icet.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public SignupResponseDTO signup(SignupRequestDTO signupRequest) {
        System.out.println("Signup attempt for: " + signupRequest.getEmail());

        // Check if user already exists by email
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use!");
        }

        // Check if username is taken
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }

        // Create new user entity
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(signupRequest.getUsername().trim());
        userEntity.setEmail(signupRequest.getEmail().trim());
        userEntity.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setEnabled(true);

        System.out.println("Saving user: " + userEntity.getUsername());
        UserEntity savedUser = userRepository.save(userEntity);


        return new SignupResponseDTO(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                "User registered successfully!"
        );
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        System.out.println("Login attempt for: " + loginRequest.getEmail());

        // Find user by email
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));

        // Check password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled!");
        }

        // Generate JWT token
        String jwt = jwtUtil.generateJwtToken(user);

        return new LoginResponseDTO(
                jwt,
                "Bearer",
                user.getUserId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return jwtUtil.validateJwtToken(token);
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public UserDTO getUserInfoFromToken(String token) {
        if (!jwtUtil.validateJwtToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        try {
            String email = jwtUtil.getEmailFromJwtToken(token);
            Long userId = jwtUtil.getUserIdFromJwtToken(token);
            String username = jwtUtil.getUsernameFromJwtToken(token);

            return new UserDTO(userId, username, email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract user info from token: " + e.getMessage());
        }
    }

    @Override
    public String refreshToken(String token) {
        if (!jwtUtil.validateJwtToken(token)) {
            throw new RuntimeException("Invalid token for refresh");
        }

        String email = jwtUtil.getEmailFromJwtToken(token);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtUtil.generateJwtToken(user);
    }

    @Override
    public void logoutUser(String token) {
        // For JWT, we don't need to store tokens, so logout is handled on client side
        // But you can add token to blacklist if needed
        System.out.println("User logged out with token: " + token.substring(0, 20) + "...");
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Validate new password
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new RuntimeException("New password must be at least 6 characters long");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        System.out.println("Password changed for user: " + userId);
    }

    @Override
    public void resetPassword(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Generate temporary password or send reset email
        // For now, just log the action
        System.out.println("Password reset requested for: " + email);

        // In a real implementation, you would:
        // 1. Generate a password reset token
        // 2. Send email with reset link
        // 3. Store the token temporarily
    }

    @Override
    public void enableUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);

        System.out.println("User enabled: " + userId);
    }

    @Override
    public void disableUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(false);
        userRepository.save(user);

        System.out.println("User disabled: " + userId);
    }

    @Override
    public boolean isUserEnabled(Long userId) {
        return userRepository.findById(userId)
                .map(UserEntity::isEnabled)
                .orElse(false);
    }
}