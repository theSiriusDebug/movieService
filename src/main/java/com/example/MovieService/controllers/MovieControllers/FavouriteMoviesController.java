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
import java.util.Optional;

@RestController
@RequestMapping
public class FavouriteMoviesController {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public FavouriteMoviesController(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @PostMapping("/favoriteMovies/{movieId}")
    public ResponseEntity<String> addFavoriteMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());


        Movie movie = movieRepository.findById(movieId);

        List<Movie> favoriteMovies = user.getFavoriteMovies();
        if (!favoriteMovies.contains(movie)) {
            favoriteMovies.add(movie);
            userRepository.save(user);
        }

        return ResponseEntity.ok("Movie added to favorites");
    }

    @DeleteMapping("/favoriteMovies/{movieId}")
    public ResponseEntity<String> removeFavoriteMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        Movie movie = movieRepository.findById(movieId);

        List<Movie> favoriteMovies = user.getFavoriteMovies();
        if (favoriteMovies.contains(movie)) {
            favoriteMovies.remove(movie);
            userRepository.save(user);
        }

        return ResponseEntity.ok("Movie removed from favorites");
    }
}