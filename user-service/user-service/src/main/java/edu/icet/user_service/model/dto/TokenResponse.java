package edu.icet.user_service.model.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String token;
    private String tokenType = "Bearer";

    public TokenResponse(String token) {
        this.token = token;
    }
}