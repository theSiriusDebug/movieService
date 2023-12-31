package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.ReviewServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Api(tags = "ReviewCreationController API")
@RestController
@RequestMapping("/reviews")
public class ReviewCreationController {
    private static final Logger logger = Logger.getLogger(ReviewCreationController.class.getName());
    private final MovieServiceImpl movieServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;

    @Autowired
    public ReviewCreationController(MovieServiceImpl movieServiceImpl, UserServiceImpl userServiceImpl, ReviewServiceImpl reviewServiceImpl) {
        this.movieServiceImpl = movieServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.reviewServiceImpl = reviewServiceImpl;
    }

    @ApiOperation("Create a review")
    @PostMapping("/create/{movieId}")
    public ResponseEntity<Review> createReview(@PathVariable Long movieId, @RequestBody String reviewText) {
        logger.info("Creating review for movie with ID: " + movieId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByOptionalUsername(authentication.getName());

        Movie movie = movieServiceImpl.findOptionalMovieById(movieId);

        if (movie == null) {
            logger.warning("Movie not found with ID: " + movieId);
            return ResponseEntity.notFound().build();
        }

        Review review = new Review();
        review.setUser(currentUser);
        review.setMovie(movie);
        review.setReviewText(reviewText);

        currentUser.getReviews().add(review);
        userServiceImpl.save(currentUser);

        logger.info("Review created successfully for movie with ID: " + movieId);
        return ResponseEntity.ok(review);
    }

    @ApiOperation("Delete a review")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        logger.info("Deleting review with ID: " + reviewId);

        Review review = reviewServiceImpl.findReviewById(reviewId);

        if (review == null) {
            logger.warning("Review not found with ID: " + reviewId);
            return ResponseEntity.badRequest().body("Review not found.");
        }

        // Check if the current user is the owner of the review
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByUsername(authentication.getName());

        //The review is carried out by a user with good reviews or an administrator.
        if ((!currentUser.hasRole("ROLE_ADMIN") && !review.getUser().equals(currentUser))) {
            logger.warning("Unauthorized deletion attempt for review with ID: " + reviewId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete this review.");
        }

        // Remove the review from the movie's review list
        Movie movie = review.getMovie();
        movie.getReviews().remove(review);
        movieServiceImpl.saveMovie(movie);

        // Remove the review from the user's review list
        User user = review.getUser();
        user.getReviews().remove(review);
        userServiceImpl.save(user);

        reviewServiceImpl.deleteReview(review);

        logger.info("Review deleted successfully with ID: " + reviewId);
        return ResponseEntity.ok("Review deleted successfully.");
    }
}