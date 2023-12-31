package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.sevices.interfaces.ReviewService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private static final Logger logger = Logger.getLogger(ReviewServiceImpl.class.getName());

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    @Override
    public List<Review> findAllReviews() {
        logger.info("Retrieving all reviews");
        return reviewRepository.findAll();
    }

    @Override
    public Review findReviewById(long id) {
        try {
            logger.info(String.format("Retrieving review with ID: %d", id));
            return reviewRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
        } catch (NotFoundException e) {
            logger.warning(String.format("Review not found with ID: %d", id));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReview(Review review) {
        logger.info(String.format("Deleting review with ID: %d", review.getId()));
        reviewRepository.delete(Objects.requireNonNull(review, "Review cannot be null."));
        logger.info("Review deleted successfully");
    }

    @Override
    public List<Review> findReviewByMovie(Movie movie) {
        List<Review> reviews = reviewRepository.findByMovie(Objects.requireNonNull(movie, "Movie cannot be null."));

        if (reviews == null) {
            logger.warning("No reviews found for movie with ID: " + movie.getId());
        } else {
            logger.info("Found " + reviews.size() + " reviews for movie with ID: " + movie.getId());
        }

        return reviews;
    }

    @Override
    public void saveReview(Review review){
        reviewRepository.save(Objects.requireNonNull(review, "Review cannot be null."));
    }
}
