package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;

public interface MovieService {
    Movie findMovieById(long id);

    Movie findOptionalMovieById(long id);

    void deleteMovie(Movie movie);

    void saveMovie(Movie movie);
}
