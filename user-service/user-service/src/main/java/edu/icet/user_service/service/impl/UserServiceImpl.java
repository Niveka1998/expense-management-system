package edu.icet.user_service.service.impl;

import edu.icet.user_service.model.dto.LoginRequestDTO;
import edu.icet.user_service.model.dto.LoginResponseDTO;
import edu.icet.user_service.model.dto.UserDTO;
import edu.icet.user_service.model.entity.UserEntity;
import edu.icet.user_service.repository.UserRepository;
import edu.icet.user_service.service.UserService;
import edu.icet.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDTO getUserByEmail(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return convertToDTO(userOptional.get());
        }
        return null;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return convertToDTO(userOptional.get());
        }
        return null;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // Validate required fields
        validateUserDTO(userDTO);

        // Check if user already exists
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        // Create new user entity
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTO.getEmail().trim());
        userEntity.setUsername(userDTO.getUsername().trim());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setEnabled(true);

        UserEntity savedUser = userRepository.save(userEntity);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        UserEntity userEntity = userOptional.get();

        // Update fields (excluding password and email for security)
        if (userDTO.getUsername() != null && !userDTO.getUsername().trim().isEmpty()) {
            // Check if new username is already taken by another user
            Optional<UserEntity> existingUser = userRepository.findByUsername(userDTO.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(userId)) {
                throw new RuntimeException("Username is already taken");
            }
            userEntity.setUsername(userDTO.getUsername().trim());
        }

        UserEntity savedUser = userRepository.save(userEntity);
        return convertToDTO(savedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // Legacy methods for backward compatibility
    @Override
    public LoginResponseDTO registerUser(UserDTO userDTO) {
        System.out.println("Legacy registerUser called for: " + userDTO.getEmail());

        UserDTO createdUser = createUser(userDTO);

        // Find the saved user to generate token
        UserEntity userEntity = userRepository.findByEmail(createdUser.getEmail())
                .orElseThrow(() -> new RuntimeException("User creation failed"));

        // Generate JWT token for new user
        String jwt = jwtUtil.generateJwtToken(userEntity);

        return new LoginResponseDTO(
                jwt,
                "Bearer",
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getEmail()
        );
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        System.out.println("Legacy login called for: " + loginRequest.getEmail());

        // Find user by email
        UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        // Check if user is enabled
        if (!userEntity.isEnabled()) {
            throw new RuntimeException("User account is disabled!");
        }

        // Generate JWT token
        String jwt = jwtUtil.generateJwtToken(userEntity);

        return new LoginResponseDTO(
                jwt,
                "Bearer",
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getEmail()
        );
    }

    // Helper methods
    private UserDTO convertToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setUsername(userEntity.getUsername());
        // Don't include password in DTO for security
        return userDTO;
    }

    private void validateUserDTO(UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
    }
}