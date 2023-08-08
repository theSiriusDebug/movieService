package com.example.MovieService.controllers.TMDBControllers;

import com.example.MovieService.models.Genre;
import com.example.MovieService.models.Movie;
import com.example.MovieService.models.api_Items.MovieApiItem;
import com.example.MovieService.models.api_Items.MovieApiResponse;
import com.example.MovieService.repositories.GenreRepository;
import com.example.MovieService.repositories.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PopularMoviesController  {
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MovieRepository movieRepository;
    private GenreRepository genreRepository;

    @Autowired
    public PopularMoviesController (MovieRepository movieRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/addMovies")
    public List<Movie> addPopularMoviesToDB() {
        List<Movie> movies = new ArrayList<>();
        boolean a = true;
        int page = 1;
        while (a == true) {
            if (page>=10){
                a=false;
            }
            String url = String.format("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=%s", page);
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkMTkzMjFkNTkxMzkwMDgwMGExYmJiMzc0NTM4NTdmZCIsInN1YiI6IjY0YzVmNjcyNjNlNmZiMDBjNDA5MmYxMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.gIQ1ZBgljNLDScxFhS5qr9KeQI0wSIOBMJfshRCsFoU")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                MovieApiResponse apiResponse = objectMapper.readValue(responseBody, MovieApiResponse.class);
                movies = mapToMovies(apiResponse);
                movieRepository.saveAll(movies);
                page++;
            } catch (IOException e) {
                e.printStackTrace();
                page++;
            }
        }
        return movies;
    }

    private List<Movie> mapToMovies(MovieApiResponse apiResponse) {
        List<Movie> movies = new ArrayList<>();
        for (MovieApiItem apiItem : apiResponse.getResults()) {
            Movie movie = new Movie();
            movie.setTitle(apiItem.getTitle());
            movie.setDescription(apiItem.getOverview());
            movie.setRelease_year(apiItem.getRelease_date());
            movie.setDirector(apiItem.getDirector());

            List<Genre> genres = new ArrayList<>();
            for (Long genreId : apiItem.getGenre_ids()) {
                Genre genre = genreRepository.findById(genreId).orElse(null);
                if (genre != null) {
                    genres.add(genre);
                } else {
                    System.out.println("Genre not found for ID: " + genreId);
                }
            }
            movie.setGenres(genres);

            movie.setDuration(apiItem.getRuntime());
            movie.setRating(apiItem.getVote_average());
            movie.setCoverImage(apiItem.getPoster_path());
            movie.setHorizontal_image(apiItem.getPoster_path());
            movie.setVideo(apiItem.getVideo_path());
            movie.setTrailer(apiItem.getTrailer_path());
            movies.add(movie);
        }
        return movies;
    }
}