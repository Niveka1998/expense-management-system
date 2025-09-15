package edu.icet.user_service.service;

import edu.icet.user_service.model.dto.*;

public interface AuthService {

    // Authentication methods
    SignupResponseDTO signup(SignupRequestDTO signupRequest);
    LoginResponseDTO login(LoginRequestDTO loginRequest);

    // Token management
    boolean validateToken(String token);
    UserDTO getUserInfoFromToken(String token);
    String refreshToken(String token);
    void logoutUser(String token);

    // Password management
    void changePassword(Long userId, String oldPassword, String newPassword);
    void resetPassword(String email);

    // Account management
    void enableUser(Long userId);
    void disableUser(Long userId);
    boolean isUserEnabled(Long userId);
}