package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> findAllMovies();

    Movie findOptionalMovieById(long id);

    void deleteMovie(Movie movie);

    void saveMovie(Movie movie);

    List<Movie>findMovieByTitle(String title, Sort sorting);

    List<Movie> findByMovieTitle(String title);

    Optional<Movie> findMovieById(Long id);

    List<Movie> findAllMoviesSorted(Sort sorting);
}
