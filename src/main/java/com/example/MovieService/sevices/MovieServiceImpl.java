package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.MovieDto;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.sevices.interfaces.MovieService;
import com.example.MovieService.utils.validator.MovieMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> findAllMovies(){
        log.info("Return all movies");
        return movieRepository.findAll();
    }

    @Override
    public List<MovieDto> findAllMovieDto(Sort sorting) {
        log.info("Retrieving all movies from the database");

        List<Movie> movies = movieRepository.findAll(sorting);

        List<MovieDto> movieDtos = movies.stream()
                .map(MovieMapper::mapToMovieDto)
                .collect(Collectors.toList());

        log.info("Found {} movies", movieDtos.size());

        return movieDtos;
    }

    @Override
    public Movie findOptionalMovieById(long id) {
        log.info("Searching for movie with ID: {}", id);
        Movie movie = Objects.requireNonNull(movieRepository.findById(id), "Movie cannot be null");

        log.info("Movie found: {}", movie.getTitle());
        return movie;
    }

    @Override
    public void saveMovie(@Valid Movie movie) {
        log.info("Saving movie: {}", movie.getTitle());
        movieRepository.save(Objects.requireNonNull(movie, "Movie cannot be null."));
        log.info("Movie saved successfully: {}", movie.getTitle());
    }

    @Override
    public List<MovieDto> findMovieByTitle(String title, Sort sorting) {
        log.info("Searching movies by title containing: {}", title);
        List<Movie> movies = movieRepository.search(title, sorting);

        List<MovieDto> movieDtos = movies.stream()
                .map(MovieMapper::mapToMovieDto)
                .collect(Collectors.toList());

        log.info("Returning {} movie DTOs for title: {}", movieDtos.size(), title);

        return movieDtos;
    }
}
