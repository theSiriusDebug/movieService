package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.sevices.MovieServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@Api(tags = "MovieCreationController API")
public class CreateMovieController {
    private final MovieServiceImpl movieServiceImpl;

    @Autowired
    public CreateMovieController(MovieServiceImpl movieServiceImpl) {
        this.movieServiceImpl = movieServiceImpl;

    }

    @ApiOperation("Create a movie")
    @PostMapping("/create")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        movieServiceImpl.saveMovie(movie);
        return ResponseEntity.ok(movie);
    }
}
