package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
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

@Api(tags = "WatchLaterMoviesController API")
@RestController
@RequestMapping("/watchLaterMovies")
public class WatchLaterMoviesController {
    private final UserServiceImpl userServiceImpl;
    private final MovieServiceImpl movieServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(WatchLaterMoviesController.class);

    @Autowired
    public WatchLaterMoviesController(UserServiceImpl userServiceImpl, MovieServiceImpl movieServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.movieServiceImpl = movieServiceImpl;
    }

    @ApiOperation("add movie to watch later list")
    @PostMapping("/{movieId}")
    public ResponseEntity<String> addWatchLaterMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userServiceImpl.findByOptionalUsername(authentication.getName());

        Movie movie = movieServiceImpl.findOptionalMovieById(movieId);
        if (movie == null) {
            logger.error("Movie with ID {} not found.", movieId);
            throw new EntityNotFoundException("Movie not found");
        }

        List<Movie> watchLaterMovies = user.getWatchLaterMovies();
        if (!watchLaterMovies.contains(movie)) {
            watchLaterMovies.add(movie);
            userServiceImpl.save(user);
            logger.info("Movie with ID {} added to watch later for user {}.", movieId, user.getUsername());
        }

        return ResponseEntity.ok("Movie added to watch later");
    }

    @ApiOperation("remove movie from watch later list")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> removeWatchLaterMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userServiceImpl.findByUsername(authentication.getName());

        Movie movie = movieServiceImpl.findOptionalMovieById(movieId);
        if (movie == null) {
            logger.error("Movie with ID {} not found.", movieId);
            throw new EntityNotFoundException("Movie not found");
        }

        List<Movie> watchLater = user.getWatchLaterMovies();
        if (watchLater.contains(movie)) {
            watchLater.remove(movie);
            userServiceImpl.save(user);
            logger.info("Movie with ID {} removed from watch later for user {}.", movieId, user.getUsername());
        }

        return ResponseEntity.ok("Movie removed from watch later");
    }
}