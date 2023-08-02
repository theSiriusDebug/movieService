package com.example.MovieService.TEST;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {
    private final OkHttpClient client = new OkHttpClient();

    @GetMapping("/popular")
    public String getPopularMovies() {
        String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=ru-US&page=1&sort_by=popularity.desc";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkMTkzMjFkNTkxMzkwMDgwMGExYmJiMzc0NTM4NTdmZCIsInN1YiI6IjY0YzVmNjcyNjNlNmZiMDBjNDA5MmYxMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.gIQ1ZBgljNLDScxFhS5qr9KeQI0wSIOBMJfshRCsFoU")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred during request";
        }
    }
}
