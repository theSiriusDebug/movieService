package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Api(tags = "FavouriteMoviesController API")
@RestController
@RequestMapping
public class FavouriteMoviesController {
    private final UserService userService;
    private final MovieServiceImpl movieServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(FavouriteMoviesController.class);

    @Autowired
    public FavouriteMoviesController(UserService userService, MovieServiceImpl movieServiceImpl) {
        this.userService = userService;
        this.movieServiceImpl = movieServiceImpl;
    }

    @ApiOperation("add movie to favourite list")
    @PostMapping("/favoriteMovies/{movieId}")
    public ResponseEntity<String> addFavoriteMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        Movie movie = movieServiceImpl.findOptionalMovieById(movieId);
        if (movie == null) {
            logger.error("Movie with ID {} not found.", movieId);
            throw new EntityNotFoundException("Movie not found");
        }

        List<Movie> favoriteMovies = user.getFavoriteMovies();
        if (!favoriteMovies.contains(movie)) {
            favoriteMovies.add(movie);
            userService.save(user);
            logger.info("Movie with ID {} added to favorites for user {}.", movieId, user.getUsername());
        }

        return ResponseEntity.ok("Movie added to favorites");
    }

    @ApiOperation("remove movie from favourite list")
    @DeleteMapping("/favoriteMovies/{movieId}")
    public ResponseEntity<String> removeFavoriteMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        Movie movie = movieServiceImpl.findOptionalMovieById(movieId);
        if (movie == null) {
            logger.error("Movie with ID {} not found.", movieId);
            throw new EntityNotFoundException("Movie not found");
        }

        List<Movie> favoriteMovies = user.getFavoriteMovies();
        if (favoriteMovies.contains(movie)) {
            favoriteMovies.remove(movie);
            userService.save(user);
            logger.info("Movie with ID {} removed from favorites for user {}.", movieId, user.getUsername());
        }

        return ResponseEntity.ok("Movie removed from favorites");
    }
}