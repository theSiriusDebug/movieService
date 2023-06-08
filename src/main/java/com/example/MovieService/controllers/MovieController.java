package com.example.MovieService.controllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;
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
        } else {
            // Обработка случая, когда фильм не найден
            // Можно добавить сообщение об ошибке или выполнить другие действия
        }
        return "movie";
    }

    @GetMapping("/watch/{id}")
    public String watchMovie(@PathVariable long id, Model model) {
        Movie movie = movieRepository.findById(id);
        if (movie != null) {
            String videoUrl = "/" + movie.getVideo(); // Assuming the video file is directly under the "static" directory
            model.addAttribute("film", videoUrl);
            return "test";
        } else {
            // Handle case when movie is not found
            return "error"; // or redirect to an error page, display a message, etc.
        }
    }
}
