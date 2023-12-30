package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Rating;
import com.example.MovieService.models.User;
import com.example.MovieService.sevices.MovieService;
import com.example.MovieService.sevices.UserService;
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

    private final MovieService movieService;
    private final UserService userService;

    @Autowired
    public RatingCreationController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @ApiOperation("Create a rating")
    @PostMapping("/create/{movieId}")
    public ResponseEntity<String> createRating(@PathVariable Long movieId, @RequestBody Rating rating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByOptionalUsername(authentication.getName());

        Movie movie = movieService.findOptionalMovieById(movieId);

        for (Rating existingRating : user.getRating()) {
            if (existingRating.getMovie().getId().equals(movieId)) {
                return new ResponseEntity<>("User has already rated this movie.", HttpStatus.BAD_REQUEST);
            }
        }

        rating.setMovie(movie);
        rating.setUser(user);
        rating.setRatingValue(rating.getRatingValue());
        user.getRating().add(rating);

        userService.save(user);

        return new ResponseEntity<>("Rating created successfully.", HttpStatus.CREATED);
    }
}