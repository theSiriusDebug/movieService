package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.MovieDto;
import com.example.MovieService.models.dtos.movieDtos.MovieFilterDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MovieService {
    List<MovieDto> filteredMovies(MovieFilterDTO dto);
    List<Movie> findAllMovies();

    List<MovieDto> findAllMovieDto(Sort sorting);

    Movie findMovieById(long id);

    void saveMovie(Movie movie);

    List<MovieDto>findMovieByTitle(String title, Sort sorting);
}
