package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "ReviewCreationController API")
@RestController
@RequestMapping("/reviews")
public class ReviewCreationController {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewCreationController(MovieRepository movieRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @ApiOperation("Create a review")
    @PostMapping("/create/{movieId}")
    public ResponseEntity<String> createReview(@PathVariable Long movieId, @RequestBody String reviewText) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        // Get a movie asset to which a review will be added
        Movie movie = movieRepository.findById(movieId).orElse(null);

        Review review = new Review();
        review.setUser(currentUser);
        review.setMovie(movie);
        review.setReviewText(reviewText);

        currentUser.getReviews().add(review);
        userRepository.save(currentUser);

        return ResponseEntity.ok("Review created successfully.");
    }

    @ApiOperation("Delete a review")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        // Find the review to be deleted
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review == null) {
            return ResponseEntity.badRequest().body("Review not found.");
        }

        // Check if the current user is the owner of the review
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        if (!review.getUser().equals(currentUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete this review.");
        }

        // Remove the review from the movie's review list
        Movie movie = review.getMovie();
        movie.getReviews().remove(review);
        movieRepository.save(movie);

        // Remove the review from the user's review list
        User user = review.getUser();
        user.getReviews().remove(review);
        userRepository.save(user);

        // Delete the review
        reviewRepository.delete(review);

        return ResponseEntity.ok("Review deleted successfully.");
    }
}