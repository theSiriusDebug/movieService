package com.example.MovieService.utils.validator;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserDto;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto mapToUserDto(User user){
        return new UserDto(
                user.getUsername(),
                user.getRoles(),
                user.getFavoriteMovies().stream().map(UserMoviesMapper::mapToUserMovieDto).collect(Collectors.toList()),
                user.getFavoriteMovies().stream().map(UserMoviesMapper::mapToUserMovieDto).collect(Collectors.toList()),
                user.getReviews().stream().map(UserReviewsMapper::mapToUserReviewsDto).collect(Collectors.toList())
        );
    }
}
