package com.example.MovieService.controllers.TEST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import static com.example.MovieService.controllers.TEST.parser.parseMovieDetails;
import static com.example.MovieService.controllers.TEST.parser.run;

@RestController
public class MovieParserController {
    private final filmRepository filmRepository;

    @Autowired
    public MovieParserController(com.example.MovieService.controllers.TEST.filmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping("/findFilms")
    public List<film> get() {
        return filmRepository.findAll();
    }

    @DeleteMapping("/removeFilms")
    public String del(){
        filmRepository.deleteAll();
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
