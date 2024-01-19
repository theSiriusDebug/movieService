package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieService {
    List<Movie> findAllMovies();

    Movie findOptionalMovieById(long id);

    void saveMovie(Movie movie);

    List<Movie>findMovieByTitle(String title, Sort sorting);

    List<Movie> findAllMoviesSorted(Sort sorting);
}
