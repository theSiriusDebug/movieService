package com.example.MovieService.utils.mappers;

import com.example.MovieService.models.Review;
import com.example.MovieService.models.dtos.ReviewDto;

public class ReviewMapper {
    public static ReviewDto mapToReviewDto(Review review){
        return new ReviewDto(
                review.getReviewText(),
                review.getUser().getUsername(),
                review.getReplies()
        );
    }
}
