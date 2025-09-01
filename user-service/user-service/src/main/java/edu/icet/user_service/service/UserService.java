package edu.icet.user_service.service;


import edu.icet.user_service.model.dto.LoginRequestDTO;
import edu.icet.user_service.model.dto.LoginResponseDTO;
import edu.icet.user_service.model.dto.UserDTO;

public interface UserService {
    LoginResponseDTO login(LoginRequestDTO loginRequest);
    LoginResponseDTO registerUser(UserDTO userDTO);
    UserDTO getUserByEmail(String email);
}