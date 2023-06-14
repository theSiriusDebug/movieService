package com.example.MovieService.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String login;
    private String password;
    private String role;

    public UserRegistrationDto(String username, String login, String password, String role) {
        this.username = username;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
