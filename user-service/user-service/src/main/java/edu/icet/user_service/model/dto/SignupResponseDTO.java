package edu.icet.user_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private String message;

    // Constructor without message (will set default message)
    public SignupResponseDTO(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.message = "User registered successfully!";
    }
}
