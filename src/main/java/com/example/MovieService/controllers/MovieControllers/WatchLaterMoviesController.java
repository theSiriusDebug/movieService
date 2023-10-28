package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping
public class WatchLaterMoviesController {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private static final Logger logger = LoggerFactory.getLogger(WatchLaterMoviesController.class);

    public WatchLaterMoviesController(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @PostMapping("/watchLaterMovies/{movieId}")
    public ResponseEntity<String> addWatchLaterMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        Movie movie = movieRepository.findById(movieId);
        if (movie == null) {
            logger.error("Movie with ID {} not found.", movieId);
            throw new EntityNotFoundException("Movie not found");
        }

        List<Movie> watchLaterMovies = user.getWatchLaterMovies();
        if (watchLaterMovies.contains(movie)) {
            watchLaterMovies.remove(movie);
            userRepository.save(user);
            logger.info("Movie with ID {} removed from watch later for user {}.", movieId, user.getUsername());
        }

        return ResponseEntity.ok("Movie added to watch later");
    }

    @DeleteMapping("/watchLaterMovies/{movieId}")
    public ResponseEntity<String> removeWatchLaterMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        Movie movie = movieRepository.findById(movieId);
        if (movie == null) {
            logger.error("Movie with ID {} not found.", movieId);
            throw new EntityNotFoundException("Movie not found");
        }

        List<Movie> watchLater = user.getWatchLaterMovies();
        if (watchLater.contains(movie)) {
            watchLater.remove(movie);
            userRepository.save(user);
            logger.info("Movie with ID {} removed from watch later for user {}.", movieId, user.getUsername());
        }

        return ResponseEntity.ok("Movie removed from watch later");
    }
}