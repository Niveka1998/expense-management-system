package edu.icet.expense_service.client;

import edu.icet.expense_service.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user/get-user/{email}")
    UserDTO getUserByEmail(@PathVariable("email") String email);
}
