package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.sevices.interfaces.MovieService;
import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @Override
    public List<Movie> findAllMovies(){
        log.info("Return all movies");
        return movieRepository.findAll();
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
    public List<Movie> findMovieByTitle(String title, Sort sorting) {
        log.debug("Searching movies by title containing: {}", title);
        if (title != null) {
            return movieRepository.search(title, sorting);
        }
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> findAllMoviesSorted(Sort sorting) {
        log.debug("Return all sorted movies");
        return movieRepository.findAll(sorting);
    }
}
