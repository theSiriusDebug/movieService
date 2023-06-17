package com.example.MovieService.controllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("movies", movieRepository.findAll());
        return "movies";
    }

    @GetMapping("/{id}")
    public String getMovieDetails(@PathVariable("id") Long id, Model model) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());

            // Получение списка комментариев для данного фильма
            List<Review> comments = reviewRepository.findByMovie(movie.get());
            for (Review comment : comments) {
                User user = comment.getUser(); // Получить связанного пользователя
                comment.setUser(user); // Установить пользователя в комментарий
            }
            model.addAttribute("comments", comments);
        } else {
            // Обработка случая, когда фильм не найден
            return "error";
        }
        return "movie";
    }

    @GetMapping("/watch/{id}")
    public String watchMovie(@PathVariable long id, Model model) {
        Movie movie = movieRepository.findById(id);
        if (movie != null) {
            String videoUrl = "/" + movie.getVideo();
            model.addAttribute("film", videoUrl);
            return "test";
        } else {
            return "error";
        }
    }

    @GetMapping("/search")
    public String searchMovie(@RequestParam("title") String title, Model model) {
        List<Movie> movies = movieRepository.findByTitleStartingWithIgnoreCase(title);
        if (!movies.isEmpty()) {
            model.addAttribute("movies", movies);
            return "search-results"; // Название нового HTML-шаблона
        } else {
            return "no-results";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "create-movie";
    }

    @PostMapping("/create")
    public String createMovie(@ModelAttribute("movie") Movie movie,
                              @RequestParam("coverImageFile") MultipartFile coverImageFile,
                              @RequestParam("videoFile") MultipartFile videoFile) {
        if (!coverImageFile.isEmpty()) {
            try {
                // Генерация уникального имени файла изображения обложки
                String uniqueCoverImageFilename = UUID.randomUUID().toString() + "-" + coverImageFile.getOriginalFilename();

                // Полный путь сохранения файла изображения обложки
                String coverImageFilePath = "src/main/resources/static/" + uniqueCoverImageFilename;

                // Сохранение файла изображения обложки на сервере
                Path coverImagePath = Paths.get(coverImageFilePath);
                Files.write(coverImagePath, coverImageFile.getBytes());

                movie.setCoverImage(uniqueCoverImageFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!videoFile.isEmpty()) {
            try {
                String uniqueVideoFilename = UUID.randomUUID().toString() + "-" + videoFile.getOriginalFilename();

                String videoFilePath = "src/main/resources/static/" + uniqueVideoFilename;

                Path videoPath = Paths.get(videoFilePath);
                Files.write(videoPath, videoFile.getBytes());

                movie.setVideo(uniqueVideoFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        movieRepository.save(movie);

        String cacheBuster = UUID.randomUUID().toString();

        return "redirect:/movies?cb=" + cacheBuster;
    }

    @PostMapping("/{id}/reviews/create")
    public String createReview(@PathVariable("id") Long movieId,
                               @RequestParam("reviewText") String reviewText,
                               Principal principal) {
        if (principal == null){
            return "error";
        }
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        // Создание нового отзыва
        Review review = new Review();
        review.setMovie(movieRepository.findById(movieId).orElse(null));
        review.setUser(user.get());
        review.setReview(reviewText);

        // Сохранение отзыва в базу данных
        reviewRepository.save(review);

        // Редирект на страницу деталей фильма
        return "redirect:/movies/" + movieId;
    }
}
