package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.repositories.ReviewRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private static final Logger logger = Logger.getLogger(ReviewService.class.getName());

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAllReviews() {
        logger.info("Retrieving all reviews");
        return reviewRepository.findAll();
    }

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

    public void deleteReview(Review review) {
        logger.info(String.format("Deleting review with ID: %d", review.getId()));
        reviewRepository.delete(review);
        logger.info("Review deleted successfully");
    }

    public List<Review> findReviewByMovie(Movie movie) {
        List<Review> reviews = reviewRepository.findByMovie(movie);

        if (reviews == null) {
            logger.warning("No reviews found for movie with ID: " + movie.getId());
        } else {
            logger.info("Found " + reviews.size() + " reviews for movie with ID: " + movie.getId());
        }

        return reviews;
    }

    public void saveReview(Review review){
        reviewRepository.save(review);
    }
}
