package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.sevices.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies/watch")
@Api(tags = "MovieStreamingController API")
public class StreamMovieController {
    private final MovieService movieService;

    @Autowired
    public StreamMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @ApiOperation("Watch movie by movie ID")
    @GetMapping("/{id}")
    public ResponseEntity<String> getMovieVideoByMovieId(@PathVariable long movieId) {
        Movie movieById = movieService.findOptionalMovieById(movieId);
        if (movieById != null) {
            String videoUrl = movieById.getMovieLink();
            return ResponseEntity.ok(videoUrl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Watch movie by trailer ID")
    @GetMapping("/trailer/{id}")
    public ResponseEntity<String> getTrailerByMovieId(@PathVariable long movieId) {
        Movie movieById = movieService.findOptionalMovieById(movieId);
        if (movieById != null) {
            String videoUrl = movieById.getTrailerLink();
            return ResponseEntity.ok(videoUrl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
