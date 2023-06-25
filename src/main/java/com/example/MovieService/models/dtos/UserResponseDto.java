package com.example.MovieService.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String username;
    public UserResponseDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
