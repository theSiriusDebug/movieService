package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.sevices.interfaces.MovieService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAllMovies(){
        log.info("Return all movies");
        return movieRepository.findAll();
    }

    @Override
    public Movie findMovieById(long id) {
        log.info("Find movie by id " + id);
        return movieRepository.findById(id);
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
    public void deleteMovie(Movie movie) {
        log.info(String.format("Deleting movie with ID: %d", movie.getId()));
        movieRepository.delete(Objects.requireNonNull(movie, "Movie cannot be null."));
        log.info("Movie deleted successfully");
    }

    @Override
    public void saveMovie(Movie movie) {
        movieRepository.save(Objects.requireNonNull(movie, "Movie cannot be null."));
        log.info("Movie saved.");
    }
}
