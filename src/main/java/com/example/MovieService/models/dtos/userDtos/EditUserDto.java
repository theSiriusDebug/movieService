package com.example.MovieService.models.dtos.userDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {
    private String username;
    private String password;
    private String new_username;
    private String new_password;
}
