package edu.icet.user_service.service;

import edu.icet.user_service.model.dto.UserDTO;
import edu.icet.user_service.model.dto.LoginRequestDTO;
import edu.icet.user_service.model.dto.LoginResponseDTO;

public interface UserService {

    // User management methods
    UserDTO getUserByEmail(String email);
    UserDTO getUserById(Long userId);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long userId);
    boolean userExists(Long userId);
    boolean emailExists(String email);
    boolean usernameExists(String username);

    // Legacy method - for backward compatibility
    LoginResponseDTO registerUser(UserDTO userDTO);
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}