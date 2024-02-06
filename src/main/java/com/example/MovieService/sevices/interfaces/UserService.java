package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserDto;
import com.example.MovieService.models.dtos.userDtos.EditUserDto;

import java.util.List;

public interface UserService {
    User addMovieToList(User user, Movie movie, List<Movie> movies);

    User updateUser(EditUserDto editUser, User user);

    User findByOptionalUsername(String username);

    User findByUsername(String username);

    User save(User user);

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);
}
