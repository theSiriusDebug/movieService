package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Review;
import com.example.MovieService.sevices.ReviewServiceImpl;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewServiceImpl reviewServiceImpl;

    @Autowired
    public ReviewsController(ReviewServiceImpl reviewServiceImpl) {
        this.reviewServiceImpl = reviewServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(){
        return ResponseEntity.ok(reviewServiceImpl.findAllReviews());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable("id") long id) throws NotFoundException {
        return ResponseEntity.ok(reviewServiceImpl.findReviewById(id));
    }
}
