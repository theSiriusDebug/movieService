package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import java.util.List;

public interface ReviewService {
    List<Review> findAllReviews();

    Review findReviewById(long id);

    void deleteReview(Review review);

    List<Review> findReviewByMovie(Movie movie);

    void saveReview(Review review);
}
