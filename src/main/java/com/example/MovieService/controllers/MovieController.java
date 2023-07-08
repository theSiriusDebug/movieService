package com.example.MovieService.controllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
@Api(tags = "Movie API")
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = Logger.getLogger(MovieController.class);

    @ApiOperation("Get all movies")
    @GetMapping
    public ResponseEntity<List<Movie>> getAll(@RequestParam(required = false) String sortType) {
        List<Movie> movies;
        if ("by date".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
        } else if ("by alphabet".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
        } else if ("rating".equals(sortType)) {
            movies = movieRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"));
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
            logger.warn(String.format("Movie not found for id: %s", id));
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Watch movie by movie ID")
    @GetMapping("/watch/{id}")
    public ResponseEntity<String> watchMovie(@PathVariable long id) {
        Movie movie = movieRepository.findById(id);
        if (movie != null) {
            String videoUrl = "/" + movie.getVideo();
            return ResponseEntity.ok(videoUrl);
        } else {
            logger.warn(String.format("Movie not found for id: %s", id));
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Watch movie by trailer ID")
    @GetMapping("/watch/trailer/{id}")
    public ResponseEntity<String> watchTrailer(@PathVariable long id) {
        Movie movie = movieRepository.findById(id);
        if (movie != null) {
            String videoUrl = "/" + movie.getTrailer();
            return ResponseEntity.ok(videoUrl);
        } else {
            logger.warn(String.format("Movie not found for id: %s", id));
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Search movies by title")
    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovie(@RequestParam("title") String title) {
        List<Movie> movies = movieRepository.findByTitleStartingWithIgnoreCase(title);
        if (!movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            logger.info(String.format("No movies found with title: %s", title));
            return ResponseEntity.noContent().build();
        }
    }

    @ApiOperation("Create a movie")
    @PostMapping("/create")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);
        return ResponseEntity.ok(movie);
    }

    @ApiOperation("Create a review for a movie")
    @PostMapping("/{id}/reviews/create")
    public ResponseEntity<Review> createReview(@PathVariable("id") Long movieId, @RequestParam("reviewText") String reviewText, Principal principal) {
        if (principal == null) {
            logger.info("Error creating review");
            return ResponseEntity.badRequest().build();
        }
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Review review = new Review();
        review.setMovie(movieRepository.findById(movieId).orElse(null));
        review.setUser(user.orElse(null));
        review.setReview(reviewText);
        reviewRepository.save(review);
        return ResponseEntity.ok(review);
    }
}
