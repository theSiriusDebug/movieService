package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.MovieDetailsDto;
import com.example.MovieService.models.dtos.MovieDto;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
import com.example.MovieService.utils.mappers.MovieDetailsMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/movies")
@Api(tags = "MovieController API")
@CrossOrigin
public class MovieController {
    private final MovieServiceImpl service;
    private final UserServiceImpl userService;

    @Autowired
    public MovieController(MovieServiceImpl service, UserServiceImpl userService) {
        this.service = service;
        this.userService = userService;
    }

    @ApiOperation("Get movie details by movie ID")
    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDto> getMovieDetails(@PathVariable("id") Long id) {
        Movie movie = service.findMovieById(id);
        userService.addToViewedMovies(
                id, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok(MovieDetailsMapper.mapToMovieDetailsDto(movie));
    }

    @ApiOperation("Get all movies")
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies(@RequestParam(required = false, defaultValue = "by date") String sortType) {
        Sort sorting = getSorting(sortType); // Get the sorting order from the request parameter
        List<MovieDto> movies = service.findAllMovieDto(sorting);
        return ResponseEntity.ok(movies);
    }

    private Sort getSorting(String sortType) { // Helper method to get the sorting order from the request parameter
        log.info("getSorting method started with sortType: {}", sortType);
        Map<String, Sort.Order> sortTypes = Map.of(
            "by_date", Sort.Order.desc("year"),
            "by_date_reverse", Sort.Order.asc("year"),
            "by_alphabet", Sort.Order.desc("title"),
            "by_alphabet_reverse", Sort.Order.asc("title"),
            "by_rating", Sort.Order.desc("imdbRating"),
            "by_rating_reverse", Sort.Order.asc("imdbRating"),
            "by_kinopoisk_rating", Sort.Order.desc("kinopoiskRating"),
            "by_kinopoisk_rating_reverse", Sort.Order.asc("kinopoiskRating")
        );

        // Get the sorting order from the request parameter
        Sort.Order order = sortTypes.getOrDefault(sortType, Sort.Order.desc("year"));
        log.info("Selected sorting order: {}", order);

        return Sort.by(order); // Create a Sort object with the specified order
    }

    @ApiOperation("Search movies")
    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> getMoviesByTitle(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "by date") String sortType
    ) {
        Sort sorting = getSorting(sortType);
        List<MovieDto> movies = service.findMovieByTitle(title, sorting);
        return ResponseEntity.ok(movies);
    }

    @ApiOperation("Get movies by filters")
    @GetMapping("/filtered")
    public ResponseEntity<List<Movie>> getFilteredAndSortedMovies(
            @RequestParam(name = "yearMin", required = false) Integer yearMin,
            @RequestParam(name = "yearMax", required = false) Integer yearMax,
            @RequestParam(name = "durationMin", required = false) String durationMin,
            @RequestParam(name = "durationMax", required = false) String durationMax,
            @RequestParam(name = "imdbRatingMin", required = false) Double imdbRatingMin,
            @RequestParam(name = "imdbRatingMax", required = false) Double imdbRatingMax,
            @RequestParam(name = "kinopoiskRatingMin", required = false) Double kinopoiskRatingMin,
            @RequestParam(name = "kinopoiskRatingMax", required = false) Double kinopoiskRatingMax,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "genres", required = false) String genres,
            @RequestParam(name = "director", required = false) String director,
            @RequestParam(name = "orderBy", defaultValue = "title") String orderBy,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ) {
        List<Movie> filteredMovies = service.findAllMovies();

        applyFilters(filteredMovies, yearMin, yearMax, durationMin, durationMax, imdbRatingMin, imdbRatingMax, kinopoiskRatingMin, kinopoiskRatingMax, language, country, genres, director);
        Comparator<Movie> comparator = determineComparator(orderBy, order);
        filteredMovies.sort(comparator);

        return ResponseEntity.ok(filteredMovies);
    }

    private void applyFilters(
            List<Movie> movies, Integer yearMin, Integer yearMax, String durationMin, String durationMax,
            Double imdbRatingMin, Double imdbRatingMax, Double kinopoiskRatingMin, Double kinopoiskRatingMax,
            String language, String country, String genres, String director) {

        if (yearMin != null || yearMax != null) {
            final int minYear = yearMin != null ? yearMin : Integer.MIN_VALUE;
            final int maxYear = yearMax != null ? yearMax : Integer.MAX_VALUE;
            movies.removeIf(movie -> movie.getYear() < minYear || movie.getYear() > maxYear);
        }

        if (imdbRatingMin != null || imdbRatingMax != null) {
            final double minImdbRating = imdbRatingMin != null ? imdbRatingMin : Double.MIN_VALUE;
            final double maxImdbRating = imdbRatingMax != null ? imdbRatingMax : Double.MAX_VALUE;
            movies.removeIf(movie -> movie.getImdbRating() < minImdbRating || movie.getImdbRating() > maxImdbRating);
        }

        if (kinopoiskRatingMin != null || kinopoiskRatingMax != null) {
            final double minKinopoiskRating = kinopoiskRatingMin != null ? kinopoiskRatingMin : Double.MIN_VALUE;
            final double maxKinopoiskRating = kinopoiskRatingMax != null ? kinopoiskRatingMax : Double.MAX_VALUE;
            movies.removeIf(movie -> movie.getKinopoiskRating() < minKinopoiskRating || movie.getKinopoiskRating() > maxKinopoiskRating);
        }

        if (durationMin != null || durationMax != null) {
            final String minDuration = durationMin != null ? durationMin : "00:00:00";
            final String maxDuration = durationMax != null ? durationMax : "99:99:99";
            movies.removeIf(movie -> !isDurationInRange(movie.getDuration(), minDuration, maxDuration));
        }

        if (language != null && !language.isEmpty()) {
            movies.removeIf(movie -> !movie.getLanguage().equalsIgnoreCase(language));
        }

        if (country != null && !country.isEmpty()) {
            movies.removeIf(movie -> !movie.getCountry().equalsIgnoreCase(country));
        }

        if (genres != null && !genres.isEmpty()) {
            List<String> genreList = Arrays.asList(genres.split(","));
            movies.removeIf(movie -> !movie.getGenres().containsAll(genreList));
        }

        if (director != null && !director.isEmpty()) {
            movies.removeIf(movie -> !movie.getDirector().equalsIgnoreCase(director));
        }
    }

    private boolean isDurationInRange(String duration, String minDuration, String maxDuration) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date movieDuration = sdf.parse(duration);
            Date min = sdf.parse(minDuration);
            Date max = sdf.parse(maxDuration);
            return movieDuration.compareTo(min) >= 0 && movieDuration.compareTo(max) <= 0;
        } catch (ParseException e) {
            return false;
        }
    }

    private Comparator<Movie> determineComparator(String orderBy, String order) {
        Comparator<Movie> comparator;
        switch (orderBy) {
            case "year":
                comparator = Comparator.comparing(Movie::getYear);
                break;
            case "duration":
                comparator = Comparator.comparing(Movie::getDuration);
                break;
            case "imdbRating":
                comparator = Comparator.comparing(Movie::getImdbRating);
                break;
            case "kinopoiskRating":
                comparator = Comparator.comparing(Movie::getKinopoiskRating);
                break;
            case "genres":
                comparator = Comparator.comparing(movie -> String.join(",", movie.getGenres()));
                break;
            default:
                comparator = Comparator.comparing(Movie::getTitle);
        }
        if (order.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
