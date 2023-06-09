package com.example.MovieService.controllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/search")
    public String searchMovie(@RequestParam("title") String title, Model model) {
        List<Movie> movies = movieRepository.findByTitleStartingWithIgnoreCase(title);
        if (!movies.isEmpty()) {
            model.addAttribute("movies", movies);
            return "search-results"; // Название нового HTML-шаблона
        } else {
            // Handle case when no movies are found
            // You can add an error message or perform other actions
            return "no-results"; // Название HTML-шаблона для случая без результатов
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
        // Получение данных из формы и сохранение фильма
        // Входные параметры:
        // - @ModelAttribute("movie") Movie movie: объект фильма, связанный с формой
        // - @RequestParam("coverImageFile") MultipartFile coverImageFile: файл изображения обложки фильма
        // - @RequestParam("videoFile") MultipartFile videoFile: файл видео фильма

        // Проверка наличия выбранного файла изображения обложки
        if (!coverImageFile.isEmpty()) {
            try {
                // Генерация уникального имени файла изображения обложки
                String uniqueCoverImageFilename = UUID.randomUUID().toString() + "-" + coverImageFile.getOriginalFilename();

                // Полный путь сохранения файла изображения обложки
                String coverImageFilePath = "src/main/resources/static/" + uniqueCoverImageFilename;

                // Сохранение файла изображения обложки на сервере
                Path coverImagePath = Paths.get(coverImageFilePath);
                Files.write(coverImagePath, coverImageFile.getBytes());

                // Сохранение имени файла изображения обложки в объекте фильма
                movie.setCoverImage(uniqueCoverImageFilename);
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки сохранения файла изображения обложки
            }
        }

        // Проверка наличия выбранного видеофайла
        if (!videoFile.isEmpty()) {
            try {
                // Генерация уникального имени видеофайла
                String uniqueVideoFilename = UUID.randomUUID().toString() + "-" + videoFile.getOriginalFilename();

                // Полный путь сохранения видеофайла
                String videoFilePath = "src/main/resources/static/" + uniqueVideoFilename;

                // Сохранение видеофайла на сервере
                Path videoPath = Paths.get(videoFilePath);
                Files.write(videoPath, videoFile.getBytes());

                // Сохранение имени видеофайла в объекте фильма
                movie.setVideo(uniqueVideoFilename);
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки сохранения видеофайла
            }
        }

        // Сохранение фильма в базу данных
        movieRepository.save(movie);

        // Генерация случайного параметра для URL-параметра запроса
        String cacheBuster = UUID.randomUUID().toString();

        return "redirect:/movies?cb=" + cacheBuster; // Перенаправление на страницу со списком фильмов с добавлением параметра cacheBuster
    }
}
