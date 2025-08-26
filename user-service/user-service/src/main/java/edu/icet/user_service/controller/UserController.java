package edu.icet.user_service.controller;

import edu.icet.user_service.model.dto.UserDTO;
import edu.icet.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/get-user/{email}")
    public UserDTO getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @PostMapping("/register-user")
    public void registerUser(@RequestBody UserDTO userDTO){
        userService.registerUser(userDTO);
    }
}
