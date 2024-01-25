package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserDto;

import java.util.List;

public interface UserService {

    User findByOptionalUsername(String username);

    User save(User user);

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);
}
