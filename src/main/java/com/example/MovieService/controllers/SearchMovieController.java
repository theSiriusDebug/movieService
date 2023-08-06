package com.example.MovieService.controllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Api(tags = "SearchMovieController API")
public class SearchMovieController {
    private MovieRepository movieRepository;

    @Autowired
    public SearchMovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @ApiOperation("Search movies by title")
    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovie(@RequestParam("title") String movieTitle) {
        List<Movie> movies = movieRepository.findByTitleStartingWithIgnoreCase(movieTitle);
        if (!movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
