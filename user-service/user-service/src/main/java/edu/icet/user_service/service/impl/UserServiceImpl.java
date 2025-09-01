package edu.icet.user_service.service.impl;

import edu.icet.user_service.model.dto.LoginRequestDTO;
import edu.icet.user_service.model.dto.LoginResponseDTO;
import edu.icet.user_service.model.dto.UserDTO;
import edu.icet.user_service.model.entity.UserEntity;
import edu.icet.user_service.repository.UserRepository;
import edu.icet.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        System.out.println("Login attempt for: " + loginRequest.getEmail());

        UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail());

        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate token
        String token = UUID.randomUUID().toString();

        // Create user DTO without password for response
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setUsername(userEntity.getUsername());
        // Don't set password in response

        return new LoginResponseDTO(token, userDTO);
    }

    @Override
    public LoginResponseDTO registerUser(UserDTO userDTO) {
        System.out.println("Got userDTO: " + userDTO);

        // Validate required fields
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        // Check if user already exists
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("User with this email already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTO.getEmail().trim());
        userEntity.setUsername(userDTO.getUsername().trim());

        // Hash password before saving - with null check
        System.out.println("Password received: " + (userDTO.getPassword() != null ? "YES" : "NO"));
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        userEntity.setPassword(hashedPassword);

        System.out.println("Entity to save: " + userEntity);
        UserEntity savedUser = userRepository.save(userEntity);

        // Generate token for new user
        String token = UUID.randomUUID().toString();

        // Create response DTO without password
        UserDTO responseUserDTO = new UserDTO();
        responseUserDTO.setUserId(savedUser.getUserId());
        responseUserDTO.setEmail(savedUser.getEmail());
        responseUserDTO.setUsername(savedUser.getUsername());
        // Don't set password in response

        return new LoginResponseDTO(token, responseUserDTO);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userEntity.getUserId());
            userDTO.setEmail(userEntity.getEmail());
            userDTO.setUsername(userEntity.getUsername());
            // Don't set password in response
            return userDTO;
        }
        return null;
    }
}