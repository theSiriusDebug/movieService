package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.dtos.ReviewDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> findAllReviews();

    Review findReviewById(long id);

    ReviewDto findReviewDtoById(long id);

    void deleteReview(Review review);

    List<Review> findReviewByMovie(Movie movie);

    void saveReview(Review review);
}
