package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.sevices.interfaces.ReviewService;
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
    public Review findReviewById(long id) {
        try {
            return reviewRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
        } catch (NotFoundException e) {
            log.info(String.format("Review not found with ID: %d", id));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReview(Review review) {
        log.info(String.format("Deleting review with ID: %d", review.getId()));
        reviewRepository.delete(Objects.requireNonNull(review, "Review cannot be null."));
        log.info("Review deleted successfully");
    }

    @Override
    public List<Review> findReviewByMovie(Movie movie) {
        List<Review> reviews = reviewRepository.findByMovie(Objects.requireNonNull(movie, "Movie cannot be null."));

        if (reviews == null) {
            log.error("No reviews found for movie with ID: " + movie.getId());
        } else {
            log.info("Found " + reviews.size() + " reviews for movie with ID: " + movie.getId());
        }

        return reviews;
    }

    @Override
    public void saveReview(Review review){
        reviewRepository.save(Objects.requireNonNull(review, "Review cannot be null."));
        log.info("Review saved successfully.");
    }
}
