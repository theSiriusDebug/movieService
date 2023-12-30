package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.sevices.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@Api(tags = "MovieCreationController API")
public class CreateMovieController {
    private final MovieService movieService;

    @Autowired
    public CreateMovieController(MovieService movieService) {
        this.movieService = movieService;

    }

    @ApiOperation("Create a movie")
    @PostMapping("/create")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        movieService.saveMovie(movie);
        return ResponseEntity.ok(movie);
    }
}
