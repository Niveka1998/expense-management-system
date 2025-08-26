package edu.icet.user_service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @JsonIgnore
    private Long userId;
    private String username;
    private String email;
    private String password;

}
