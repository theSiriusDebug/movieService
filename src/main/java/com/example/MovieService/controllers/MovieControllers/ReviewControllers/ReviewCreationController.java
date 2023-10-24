package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "ReviewCreationController API")
@RestController
@RequestMapping("/reviews")
public class ReviewCreationController {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewCreationController(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @ApiOperation("Create a review")
    @PostMapping("/create/{movieId}")
    public ResponseEntity<String> createReview(@PathVariable Long movieId, @RequestBody String reviewText) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());

        // Получение объекта фильма, к которому будет добавлен отзыв
        Movie movie = movieRepository.findById(movieId).orElse(null);

        // Создание нового отзыва
        Review review = new Review();
        review.setUser(currentUser);
        review.setMovie(movie);
        review.setReviewText(reviewText);

        // Добавление отзыва в списки
        currentUser.getReviews().add(review);
        // Сохранение изменений в базе данных
        userRepository.save(currentUser);

        return ResponseEntity.ok("Review created successfully.");
    }
}