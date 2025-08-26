package edu.icet.user_service.service;

import edu.icet.user_service.model.dto.UserDTO;

public interface UserService {
    void registerUser(UserDTO userDTO);
    UserDTO getUserByEmail(String email);
}
