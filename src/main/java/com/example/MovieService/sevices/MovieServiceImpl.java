package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.MovieDto;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.sevices.interfaces.MovieService;
import com.example.MovieService.utils.validator.MovieMapper;
import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        log.info("Return all movies");
        List<Movie> movies = movieRepository.findAll(sorting);
        return movies.stream()
                .map(MovieMapper::mapToMovieDto)
                .collect(Collectors.toList());
    }

    @Override
    public Movie findOptionalMovieById(long id) {
        log.info(String.format("Searching for movie with ID: %s", id));

        try {
            Movie movie = Optional.ofNullable(movieRepository.findById(id))
                    .orElseThrow(() -> new NotFoundException("Movie not found"));
            log.info(String.format("Movie found: %s", movie));
            return movie;
        } catch (NotFoundException e) {
            log.info(String.format("Movie not found for ID: %s", id));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveMovie(@Valid Movie movie) {
        log.info("Saving movie: {}", movie.getTitle());
        movieRepository.save(Objects.requireNonNull(movie, "Movie cannot be null."));
        log.info("Movie saved successfully: {}", movie.getTitle());
    }

    @Override
    public List<MovieDto> findMovieByTitle(String title, Sort sorting) {
        log.debug("Searching movies by title containing: {}", title);
        List<Movie> movies = movieRepository.search(title, sorting);
        return movies.stream()
                .map(MovieMapper::mapToMovieDto)
                .collect(Collectors.toList());
    }
}
