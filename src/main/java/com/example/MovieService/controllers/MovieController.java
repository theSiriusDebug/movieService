package com.example.MovieService.controllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.repositories.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    private static final Logger logger = Logger.getLogger(MovieController.class);

    @GetMapping
    public String getAll(@RequestParam(required = false) String sortType, Model model) {
        if ("by date".equals(sortType)) {
            model.addAttribute("movies", movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title")));
        } else if ("by alphabet".equals(sortType)) {
            model.addAttribute("movies", movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title")));
        } else if ("rating".equals(sortType)) {
            model.addAttribute("movies", movieRepository.findAll(Sort.by(Sort.Direction.DESC, "rating")));
        } else {
            model.addAttribute("movies", movieRepository.findAll());
        }
        return "movies";
    }


    @GetMapping("/{id}")
    public String getMovieDetails(@PathVariable("id") Long id, Model model) {
        logger.info(String.format("Getting movie details for id: %s", id));

        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());
            List<Review> comments = reviewRepository.findByMovie(movie.get());
            for (Review comment : comments) {
                User user = comment.getUser();
                comment.setUser(user);
            }
            model.addAttribute("comments", comments);
        } else {
            logger.warn(String.format("Movie not found for id: %s", id));
            return "error";
        }
        return "movie";
    }

    @GetMapping("/watch/{id}")
    public String watchMovie(@PathVariable long id, Model model) {
        logger.info(String.format("Watching movie with id: %s", id));

        Movie movie = movieRepository.findById(id);
        if (movie != null) {
            String videoUrl = "/" + movie.getVideo();
            model.addAttribute("film", videoUrl);
            return "test";
        } else {
            logger.warn(String.format("Movie not found for id: %s", id));

            return "error";
        }
    }

    @GetMapping("/watch/trailer/{id}")
    public String watchTrailer(@PathVariable long id, Model model) {
        logger.info(String.format("Watching trailer with id: %s", id));

        Movie movie = movieRepository.findById(id);
        if (movie != null) {
            String videoUrl = "/" + movie.getTrailer();
            model.addAttribute("trailer", videoUrl);
            return "trailer";
        } else {
            logger.warn(String.format("Movie not found for id: %s", id));

            return "error";
        }
    }


    @GetMapping("/search")
    public String searchMovie(@RequestParam("title") String title, Model model) {
        logger.info(String.format("Searching for movies with title: %s", title));

        List<Movie> movies = movieRepository.findByTitleStartingWithIgnoreCase(title);
        if (!movies.isEmpty()) {
            model.addAttribute("movies", movies);
            return "search-results"; // Название нового HTML-шаблона
        } else {
            logger.info(String.format("No movies found with title: %s", title));

            return "no-results";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Showing create form");

        model.addAttribute("movie", new Movie());
        return "create-movie";
    }

    @PostMapping("/create")
    public String createMovie(@ModelAttribute("movie") Movie movie,
                              @RequestParam("coverImageFile") MultipartFile coverImageFile,
                              @RequestParam("videoFile") MultipartFile videoFile,
                              @RequestParam("trailerFile") MultipartFile trailerFile) {
        if (!coverImageFile.isEmpty()) {
            try {
                logger.info(String.format("Creating movie: %s", movie.getTitle()));
                String uniqueCoverImageFilename = UUID.randomUUID().toString() + "-" + coverImageFile.getOriginalFilename();

                String coverImageFilePath = "src/main/resources/static/" + uniqueCoverImageFilename;

                Path coverImagePath = Paths.get(coverImageFilePath);
                Files.write(coverImagePath, coverImageFile.getBytes());

                movie.setCoverImage(uniqueCoverImageFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!videoFile.isEmpty()) {
            try {
                logger.info(String.format("Video file, by title: %s is empty", movie.getTitle()));
                String uniqueVideoFilename = UUID.randomUUID().toString() + "-" + videoFile.getOriginalFilename();

                String videoFilePath = "src/main/resources/static/" + uniqueVideoFilename;

                Path videoPath = Paths.get(videoFilePath);
                Files.write(videoPath, videoFile.getBytes());

                movie.setVideo(uniqueVideoFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!trailerFile.isEmpty()) {
            try {
                logger.info(String.format("Trailer file, by title: %s is empty", movie.getTitle()));
                String uniqueVideoFilename = UUID.randomUUID().toString() + "-" + videoFile.getOriginalFilename();

                String videoFilePath = "src/main/resources/static/" + uniqueVideoFilename;

                Path videoPath = Paths.get(videoFilePath);
                Files.write(videoPath, videoFile.getBytes());

                movie.setTrailer(uniqueVideoFilename);
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
        logger.info(String.format("Creating review: %s for movie: %s", reviewText, movieRepository.findById(movieId)));

        if (principal == null){
            logger.info("Error creating review");
            return "error";
        }
        String username = principal.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Review review = new Review();
        review.setMovie(movieRepository.findById(movieId).orElse(null));
        review.setUser(user.get());
        review.setReview(reviewText);

        reviewRepository.save(review);

        return "redirect:/movies/" + movieId;
    }
}
