package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.User;

public interface UserService {
    User findByUsername(String username);

    User findByOptionalUsername(String username);

    User save(User user);
}
