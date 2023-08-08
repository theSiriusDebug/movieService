package com.example.MovieService.controllers.MovieControllers.GenreControllers;

import com.example.MovieService.models.Genre;
import com.example.MovieService.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GenreController {
    private GenreRepository genreRepository;

    @Autowired
    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping("/addGenres")
    public String addGenresToGheGenreTable() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(28, "Action"));
        genres.add(new Genre(12, "Adventure"));
        genres.add(new Genre(16, "Animation"));
        genres.add(new Genre(35, "Comedy"));
        genres.add(new Genre(80, "Crime"));
        genres.add(new Genre(99, "Documentary"));
        genres.add(new Genre(18, "Drama"));
        genres.add(new Genre(10751, "Family"));
        genres.add(new Genre(14, "Fantasy"));
        genres.add(new Genre(36, "History"));
        genres.add(new Genre(27, "Horror"));
        genres.add(new Genre(10402, "Music"));
        genres.add(new Genre(9648, "Mystery"));
        genres.add(new Genre(10749, "Romance"));
        genres.add(new Genre(878, "Science Fiction"));
        genres.add(new Genre(10770, "TV Movie"));
        genres.add(new Genre(53, "Thriller"));
        genres.add(new Genre(10752, "War"));
        genres.add(new Genre(37, "Western"));

        genreRepository.deleteAllInBatch();
        genreRepository.saveAll(genres);

        return "Genres inserted successfully.";
    }

    @GetMapping("/genretest")
    public List<Genre>genreList(){
        return genreRepository.findAll();
    }
}
