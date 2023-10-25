package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Rating;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "RatingCreationController API")
@RestController
@RequestMapping("/rating")
public class RatingCreationController {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    public RatingCreationController(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @ApiOperation("Create a rating")
    @PostMapping("/create/{movieId}/{userId}")
    public ResponseEntity<String> createRating(@PathVariable Long movieId, @PathVariable Long userId, @RequestBody Rating rating) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (movie == null || user == null) {
            return new ResponseEntity<>("Movie or User not found.", HttpStatus.NOT_FOUND);
        }

        for (Rating existingRating : user.getRating()) {
            if (existingRating.getMovie().getId().equals(movieId)) {
                return new ResponseEntity<>("User has already rated this movie.", HttpStatus.BAD_REQUEST);
            }
        }

        rating.setMovie(movie);
        rating.setUser(user);
        user.getRating().add(rating);

        userRepository.save(user);

        return new ResponseEntity<>("Rating created successfully.", HttpStatus.CREATED);
    }
}