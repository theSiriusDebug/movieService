package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.MovieDto;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MovieService {
    List<Movie> findAllMovies();

    List<MovieDto> findAllMovieDto(Sort sorting);

    Movie findOptionalMovieById(long id);

    void saveMovie(Movie movie);

    List<Movie>findMovieByTitle(String title, Sort sorting);
}
