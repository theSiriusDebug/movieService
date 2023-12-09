package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Review;
import com.example.MovieService.repositories.ReviewRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {
    @Autowired
    private ReviewRepository reviewRepository;
    @GetMapping
    public ResponseEntity<List<Review>> getReviews(){
        return ResponseEntity.ok(reviewRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable("id") long id) throws NotFoundException {
        return ResponseEntity.ok(reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found!")));
    }
}
