package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.sevices.interfaces.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> findAllReviews() {
        log.info("Return all replies");
        return reviewRepository.findAll();
    }

    @Override
    public Review findReviewById(@Min(1) long id) {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
            log.info("Review found: {}", review);
            return review;
        } catch (NotFoundException e) {
            log.info("Review not found with ID: {}", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReview(@Valid Review review) {
        log.info("Deleting review with ID: {}", review.getId());
        reviewRepository.delete(Objects.requireNonNull(review, "Review cannot be null."));
        log.info("Review deleted successfully");
    }

    @Override
    public List<Review> findReviewByMovie(@Valid Movie movie) {
        List<Review> reviews = reviewRepository.findByMovie(Objects.requireNonNull(movie, "Movie cannot be null."));

        if (reviews == null) {
            log.error("No reviews found for movie with ID: " + movie.getId());
        } else {
            log.info("Found " + reviews.size() + " reviews for movie with ID: " + movie.getId());
        }

        return reviews;
    }

    @Override
    public void saveReview(@Valid Review review){
        reviewRepository.save(Objects.requireNonNull(review, "Review cannot be null."));
        log.info("Review saved successfully.");
    }
}
