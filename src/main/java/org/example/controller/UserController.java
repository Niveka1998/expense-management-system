package org.example.controller;

import org.example.model.dto.UserDTO;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/get-user")
    public UserDTO getUserByEmail(@RequestBody String email){
        return userService.getUserByEmail(email);
    }

    @PostMapping("/register-user")
    public void registerUser(@RequestBody UserDTO userDTO){
        userService.registerUser(userDTO);
    }
}
