package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@Api(tags = "MovieCreationController API")
public class CreateMovieController {
    @Autowired
    private MovieRepository movieRepository;

    @ApiOperation("Create a movie")
    @PostMapping("/create")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);
        return ResponseEntity.ok(movie);
    }
}
