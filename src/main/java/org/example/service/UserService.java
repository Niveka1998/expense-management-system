package org.example.service;

import org.example.model.dto.UserDTO;

public interface UserService {
    void registerUser(UserDTO userDTO);
    UserDTO getUserByEmail(String email);
}
