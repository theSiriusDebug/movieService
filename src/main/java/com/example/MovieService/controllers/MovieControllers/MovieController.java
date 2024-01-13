package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.ReviewServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/movies")
@Api(tags = "MovieController API")
@CrossOrigin
public class MovieController {
    private final MovieServiceImpl movieServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;

    @Autowired
    public MovieController(MovieServiceImpl movieServiceImpl, ReviewServiceImpl reviewServiceImpl) {
        this.movieServiceImpl = movieServiceImpl;
        this.reviewServiceImpl = reviewServiceImpl;
    }

    @ApiOperation("Get all movies")
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies(
            @RequestParam(required = false, defaultValue = "by date") String sortType,
            @RequestParam(required = false, defaultValue = "imdbRating") String sortBy) {

        Sort sorting = determineSorting(sortType, sortBy);
        List<Movie> movies = movieServiceImpl.findAllMoviesSorted(sorting);

        return ResponseEntity.ok(movies);
    }

    @ApiOperation("Search movies")
    @GetMapping("/search")
    public ResponseEntity<List<Movie>> getMoviesByTitle(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) String sortBy) {

        Sort sorting = determineSorting(sortType, sortBy);
        List<Movie> movies;

        if (title != null && !title.isEmpty()) {
            movies = movieServiceImpl.findMovieByTitle(title, sorting);
        } else {
            movies = movieServiceImpl.findAllMoviesSorted(sorting);
        }

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
        List<Movie> filteredMovies = movieServiceImpl.findAllMovies();

        applyFilters(filteredMovies, yearMin, yearMax, durationMin, durationMax, imdbRatingMin, imdbRatingMax, kinopoiskRatingMin, kinopoiskRatingMax, language, country, genres, director);
        Comparator<Movie> comparator = determineComparator(orderBy, order);
        filteredMovies.sort(comparator);

        return ResponseEntity.ok(filteredMovies);
    }


    @ApiOperation("Get movie details by movie ID")
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieDetails(@PathVariable("id") Long id) {
        Optional<Movie> movie = movieServiceImpl.findMovieById(id);
        if (movie.isPresent()) {
            List<Review> comments = reviewServiceImpl.findReviewByMovie(movie.get());
            for (Review comment : comments) {
                User user = comment.getUser();
                comment.setUser(user);
            }
            movie.get().setReviews(comments);
            return ResponseEntity.ok(movie.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private static final Map<String, Sort.Order> SORT_TYPE_TO_ORDER = new HashMap<>();
    static {
        SORT_TYPE_TO_ORDER.put("by date", Sort.Order.desc("year"));
        SORT_TYPE_TO_ORDER.put("by date reverse", Sort.Order.asc("year"));
        SORT_TYPE_TO_ORDER.put("by alphabet", Sort.Order.asc("title"));
        SORT_TYPE_TO_ORDER.put("by alphabet reverse", Sort.Order.desc("title"));
        SORT_TYPE_TO_ORDER.put("rating", Sort.Order.desc("imdbRating"));
        SORT_TYPE_TO_ORDER.put("rating reverse", Sort.Order.asc("imdbRating"));
        SORT_TYPE_TO_ORDER.put("by title", Sort.Order.asc("title"));
        SORT_TYPE_TO_ORDER.put("by title reverse", Sort.Order.desc("title"));
    }

    private Sort determineSorting(String sortType, String sortBy) {
        Sort sorting = Sort.unsorted();
        Sort.Order order = SORT_TYPE_TO_ORDER.get(sortType);
        if (order != null) {
            sorting = Sort.by(order);
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            sorting = sorting.and(Sort.by(sortBy));
        }
        return sorting;
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
