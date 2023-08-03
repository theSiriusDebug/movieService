package com.example.MovieService.controllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.ReviewRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
@Api(tags = "MovieController API")
@CrossOrigin
public class MovieController {
    private MovieRepository movieRepository;
    private ReviewRepository reviewRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
    }

    @ApiOperation("Get all movies")
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies(@RequestParam(required = false) String sortType) {
        List<Movie> movies;
        if ("by date".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
        } else if ("by date reverse".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));

        } else if ("by alphabet".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
        } else if ("by alphabet reverse".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));

        } else if ("rating".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"));
        } else if ("rating reverse".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.ASC, "rating"));
        } else {
            movies = movieRepository.findAll();
        }
        return ResponseEntity.ok(movies);
    }

    @ApiOperation("Get movie details by movie ID")
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieDetails(@PathVariable("id") Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            List<Review> comments = reviewRepository.findByMovie(movie.get());
            for (Review comment : comments) {
                User user = comment.getUser();
                comment.setUser(user);
            }
            movie.get().setReviews(comments);
            return ResponseEntity.ok(movie.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
