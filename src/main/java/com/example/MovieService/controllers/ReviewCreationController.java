package com.example.MovieService.controllers;

import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
@RestController
@RequestMapping("/movies")
@Api(tags = "ReviewCreationController API")
public class ReviewCreationController {
    private MovieRepository movieRepository;
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;

    @Autowired
    public ReviewCreationController(MovieRepository movieRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @ApiOperation("Create a review for a movie")
    @PostMapping("/{id}/reviews/create")
    public ResponseEntity<Review> createReview(@PathVariable("id") Long movieId, @RequestParam("reviewText") String reviewText, Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Review review = new Review();
        review.setMovie(movieRepository.findById(movieId).orElse(null));
        review.setUser(user);
        review.setReview(reviewText);
        reviewRepository.save(review);
        return ResponseEntity.ok(review);
    }
}
