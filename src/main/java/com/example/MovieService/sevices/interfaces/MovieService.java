package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> findAllMovies();

    Movie findOptionalMovieById(long id);

    void deleteMovie(Movie movie);

    void saveMovie(Movie movie);
}
