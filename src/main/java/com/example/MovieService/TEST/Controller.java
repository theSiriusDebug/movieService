package com.example.MovieService.TEST;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MovieRepository movieRepository;

    @Autowired
    public Controller(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/popular")
    public List<Movie> getPopularMovies() {
        List<Movie> movies = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=ru-US&page=1&sort_by=popularity.desc";
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
            movieRepository.saveAll(movies); // Save all movies to the database
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
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

            // Check if genres and actors are not null before joining
            movie.setGenre(apiItem.getGenres() != null ? String.join(", ", apiItem.getGenres()) : "");
            movie.setActors(apiItem.getActors() != null ? String.join(", ", apiItem.getActors()) : "");

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
