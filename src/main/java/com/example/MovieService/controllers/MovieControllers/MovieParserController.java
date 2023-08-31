package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import static com.example.MovieService.controllers.MovieControllers.TEST.parser.parseMovieDetails;
import static com.example.MovieService.controllers.MovieControllers.TEST.parser.run;

@RestController
public class MovieParserController {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieParserController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/findFilms")
    public List<Movie> get() {
        return movieRepository.findAll();
    }

    @DeleteMapping("/removeFilms")
    public String del(){
        movieRepository.deleteAll();
        return "delete successfully!";
    }
    @PostMapping("/parserFilms")
    public void ds() {
        long latestPage = 10;
        long currentPage = 1;
        boolean switcher = true;

        while (switcher) {
            try {
                String html = run(String.format("https://mw.anwap.tube/films/%d", currentPage));
                parseMovieDetails(html);
                System.out.println("Successfully!");
            } catch (IOException e) {
                System.out.println("Error!");
                throw new RuntimeException(e);
            }
            if (currentPage == latestPage) {
                break;
            }
            currentPage++;
        }
    }
}
