package edu.icet.category_service.client;


import edu.icet.category_service.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user/get-user/{email}")
    UserDTO getUserByEmail(@PathVariable("email") String email);

    @PostMapping("/auth/validate")
    Boolean validateToken(@RequestHeader("Authorization") String token);

    @GetMapping("/auth/user-info")
    UserDTO getUserInfo(@RequestHeader("Authorization") String token);
}