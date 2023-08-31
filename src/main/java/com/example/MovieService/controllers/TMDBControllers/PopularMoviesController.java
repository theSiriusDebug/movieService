//package com.example.MovieService.controllers.TMDBControllers;
//
//import com.example.MovieService.models.Genre;
//import com.example.MovieService.models.Movie;
//import com.example.MovieService.models.api_Items.MovieApiItem;
//import com.example.MovieService.models.api_Items.MovieApiResponse;
//import com.example.MovieService.repositories.GenreRepository;
//import com.example.MovieService.repositories.MovieRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.HttpClientErrorException;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class PopularMoviesController  {
//    private final OkHttpClient httpClient = new OkHttpClient();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final MovieRepository movieRepository;
//    private final GenreRepository genreRepository;
//    private static final Logger logger = LoggerFactory.getLogger(PopularMoviesController.class);
//
//    @Autowired
//    public PopularMoviesController (MovieRepository movieRepository, GenreRepository genreRepository) {
//        this.movieRepository = movieRepository;
//        this.genreRepository = genreRepository;
//    }
//
//    @GetMapping("/addMovies")
//    public List<Movie> addPopularMoviesToDB() {
//        List<Movie> movies = new ArrayList<>();
//        boolean continueFetching = true;
//        long currentPage = 1;
//        long maxPages = 1;
//        while (continueFetching && currentPage < maxPages) {
//            String apiUrl = String.format("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=%d", currentPage);
//            Request request = new Request.Builder()
//                    .url(apiUrl)
//                    .get()
//                    .addHeader("accept", "application/json")
//                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkMTkzMjFkNTkxMzkwMDgwMGExYmJiMzc0NTM4NTdmZCIsInN1YiI6IjY0YzVmNjcyNjNlNmZiMDBjNDA5MmYxMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.gIQ1ZBgljNLDScxFhS5qr9KeQI0wSIOBMJfshRCsFoU")
//                    .build();
//
//            try {
//                Response response = httpClient.newCall(request).execute();
//                String responseBody = response.body().string();
//                MovieApiResponse apiResponse = objectMapper.readValue(responseBody, MovieApiResponse.class);
//                movies = mapApiResponseToMovies(apiResponse);
//                movieRepository.saveAll(movies);
//                logger.info("Fetched and saved movies for page {}", currentPage);
//                currentPage++;
//            } catch (HttpClientErrorException e) {
//                logger.error("An error occurred while processing the API response: {}", e.getResponseBodyAsString(), e);
//            } catch (Exception e) {
//                logger.error("An error occurred: {}", e.getMessage(), e);
//            }        }
//        return movies;
//    }
//
//    private List<Movie> mapApiResponseToMovies(MovieApiResponse apiResponse) {
//        List<Movie> mappedMovies = new ArrayList<>();
//        for (MovieApiItem apiItem : apiResponse.getResults()) {
//            mappedMovies.add(mapApiItemToMovie(apiItem));
//        }
//        return mappedMovies;
//    }
//
//    private Movie mapApiItemToMovie(MovieApiItem apiItem) {
//        Movie movie = new Movie();
//        movie.setTitle(apiItem.getTitle());
//        movie.setDescription(apiItem.getOverview());
//        movie.setRelease_year(apiItem.getRelease_date());
//        movie.setDirector(apiItem.getDirector());
//
//        List<Genre> genres = mapGenreIdsToGenres(apiItem.getGenre_ids());
//        movie.setGenres(genres);
//
//        movie.setDuration(apiItem.getRuntime());
//        movie.setRating(apiItem.getVote_average());
//        movie.setCoverImage(apiItem.getPoster_path());
//        movie.setHorizontal_image(apiItem.getPoster_path());
//        movie.setVideo(apiItem.getVideo_path());
//        movie.setTrailer(apiItem.getTrailer_path());
//
//        return movie;
//    }
//
//    private List<Genre> mapGenreIdsToGenres(List<Long> genreIds) {
//        List<Genre> genres = new ArrayList<>();
//        for (Long genreId : genreIds) {
//            Genre genre = genreRepository.findById(genreId).orElse(null);
//            if (genre != null) {
//                genres.add(genre);
//            } else {
//                logger.warn("Genre not found for ID: {}", genreId);
//            }
//        }
//        return genres;
//    }
//}