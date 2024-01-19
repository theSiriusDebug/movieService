package com.example.MovieService.sevices;

import com.example.MovieService.models.Review;
import com.example.MovieService.models.dtos.ReviewDto;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.sevices.interfaces.ReviewService;
import com.example.MovieService.utils.validator.ReviewMapper;
import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ReviewDto> findAllReviews() {
        log.info("Return all replies");
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(ReviewMapper::mapToReviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public Review findReviewById(long id) {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
            log.info("Review found: {}", review.getId());
            return review;
        } catch (NotFoundException e) {
            log.info("Review not found with ID: {}", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReviewDto findReviewDtoById(long id) {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
            log.info("Review found: {}", review.getId());
            return ReviewMapper.mapToReviewDto(review);
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
    public void saveReview(@Valid Review review){
        reviewRepository.save(Objects.requireNonNull(review, "Review cannot be null."));
        log.info("Review saved successfully.");
    }
}
