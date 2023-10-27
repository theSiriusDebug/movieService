package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.UserRepository;
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

    public WatchLaterMoviesController(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @PostMapping("/watchLaterMovies/{movieId}")
    public ResponseEntity<String> addWatchLaterMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());


        Movie movie = movieRepository.findById(movieId);

        List<Movie> watchLaterMovies = user.getWatchLaterMovies();
        if (!watchLaterMovies.contains(movie)) {
            watchLaterMovies.add(movie);
            userRepository.save(user);
        }

        return ResponseEntity.ok("Movie added to watch later");
    }

    @DeleteMapping("/watchLaterMovies/{movieId}")
    public ResponseEntity<String> removeWatchLaterMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        Movie movie = movieRepository.findById(movieId);

        List<Movie> watchLaterMovies = user.getWatchLaterMovies();
        if (watchLaterMovies.contains(movie)) {
            watchLaterMovies.remove(movie);
            userRepository.save(user);
        }

        return ResponseEntity.ok("Movie removed from watch later");
    }
}