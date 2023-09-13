package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.controllers.MovieControllers.TEST.MovieParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class MovieParserController {
    private final MovieRepository movieRepository;
    private final MovieParser movieParser;

    @Autowired
    public MovieParserController(MovieRepository movieRepository, MovieParser movieParser) {
        this.movieRepository = movieRepository;
        this.movieParser = movieParser;
    }

    @GetMapping("/list")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @PostMapping("/parse")
    public void parseMovies() {
        long latestPage = 100;
        long currentPage = 1;

        while (currentPage <= latestPage) {
            try {
                String url = String.format("https://m.anwap.love/films/%d", currentPage);
                String html = movieParser.fetchHtml(url);
                movieParser.parseMovieDetails(html);
                System.out.println("Successfully parsed page " + currentPage);
            } catch (IOException e) {
                System.out.println("Error parsing page " + currentPage);
                throw new RuntimeException(e);
            }
            currentPage++;
        }
    }
}
