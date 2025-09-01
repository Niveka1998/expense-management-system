package edu.icet.user_service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {
    private Long userId;
    private String email;
    private String username;
    private String password;

    // Constructors
    public UserDTO() {}

    public UserDTO(Long userId, String email, String username) {
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + (password != null ? "[PROVIDED]" : "null") + '\'' +
                '}';
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
