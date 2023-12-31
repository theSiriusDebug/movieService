package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public interface UserService {
    User findByUsername(String username);

    User findByOptionalUsername(String username);

    User save(User user);
}
