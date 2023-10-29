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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PostMapping("/create/{movieId}")
    public ResponseEntity<String> createRating(@PathVariable Long movieId, @RequestBody Rating rating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        Movie movie = movieRepository.findById(movieId).orElse(null);

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
        rating.setRatingValue(rating.getRatingValue());
        user.getRating().add(rating);

        userRepository.save(user);

        return new ResponseEntity<>("Rating created successfully.", HttpStatus.CREATED);
    }
}