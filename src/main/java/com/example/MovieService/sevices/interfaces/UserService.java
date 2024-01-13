package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);

    User findByOptionalUsername(String username);

    User save(User user);

    List<User> getAllUsers();

    User getUserById(Long id);
}
