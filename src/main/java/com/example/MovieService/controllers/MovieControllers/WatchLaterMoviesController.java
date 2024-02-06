package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "WatchLaterMoviesController API")
@RestController
@RequestMapping("/watchLaterMovies")
public class WatchLaterMoviesController {
    private final UserServiceImpl userService;
    private final MovieServiceImpl movieService;

    @Autowired
    public WatchLaterMoviesController(UserServiceImpl userService, MovieServiceImpl movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }

    @ApiOperation("add movie to watch later list")
    @PostMapping("/{movieId}")
    public ResponseEntity<String> addWatchLaterMovie(@PathVariable("movieId") long id) {
        User user = userService.findByOptionalUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Movie movie = movieService.findOptionalMovieById(id);
        userService.addMovieToList(user, movie, user.getWatchLaterMovies());

        return ResponseEntity.ok("Movie added to watch later");
    }

    @ApiOperation("remove movie from watch later list")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> removeWatchLaterMovie(@PathVariable("movieId") long movieId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByOptionalUsername(authentication.getName());

        Movie movie = movieService.findOptionalMovieById(movieId);

        List<Movie> watchLater = user.getWatchLaterMovies();
        if (watchLater.contains(movie)) {
            watchLater.remove(movie);
            userService.save(user);
        }

        return ResponseEntity.ok("Movie removed from watch later");
    }
}