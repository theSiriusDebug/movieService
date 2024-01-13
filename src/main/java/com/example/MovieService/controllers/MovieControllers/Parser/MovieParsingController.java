package com.example.MovieService.controllers.MovieControllers.Parser;

import com.example.MovieService.parser.MovieDetailsExtractor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@Api(tags = "ParserController API")
public class MovieParsingController {
    private final MovieDetailsExtractor movieDetailsExtractor;

    @Autowired
    public MovieParsingController(MovieDetailsExtractor movieDetailsExtractor) {

        this.movieDetailsExtractor = movieDetailsExtractor;
    }

    @ApiOperation("Parse movie details")
    @PostMapping("/parse")
    public void parseMovies() {
        long latestPage = 1;
        long currentPage = 1;

        while (currentPage <= latestPage) {
            try {
                String url = String.format("https://m.anwap.love/films/%d", currentPage);
                String html = movieDetailsExtractor.fetchHtml(url);
                movieDetailsExtractor.parseMovieDetails(html);
                log.info("Successfully parsed page {}", currentPage);
            } catch (IOException e) {
                log.info("Error parsing page {}", currentPage);
                throw new RuntimeException(e);
            }
            currentPage++;
        }
    }
}
