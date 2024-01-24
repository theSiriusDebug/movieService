package com.example.MovieService.models.dtos;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private Set<Role> roles;
    private List<Movie> favoriteMovies;
    private List<Movie> watchLaterMovies;
    private List<UserReviewsDto> reviews;
}
