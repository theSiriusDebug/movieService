package com.example.MovieService.controllers.MovieControllers.Parser;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MovieDetailsExtractor {
    private final MovieRepository movieRepository;
    private final OkHttpClient client;

    @Autowired
    public MovieDetailsExtractor(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.client = new OkHttpClient();
    }

    public String fetchHtml(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public void parseMovieDetails(String html) {
        Document document = Jsoup.parse(html);
        Elements movieRows = document.select("tr");

        Movie movie = new Movie();

        for (Element row : movieRows) {
            String fieldName = row.select("td:nth-child(1)").text();
            switch (fieldName) {
                case "Оригинал:":
                    MovieDataParser.setOriginalTitle(row, movie);
                    break;
                case "Год:":
                    MovieDataParser.setYear(row, movie);
                    break;
                case "Качество:":
                    MovieDataParser.setQuality(row, movie);
                    break;
                case "Перевод:":
                    MovieDataParser.setLanguage(row, movie);
                    break;
                case "Время:":
                    MovieDataParser.setDuration(row, movie);
                    break;
                case "Страна:":
                    MovieDataParser.setCountry(row, movie);
                    break;
                case "Жанр:":
                    MovieDataParser.setGenres(row, movie);
                    break;
                case "Рейтинг:":
                    MovieDataParser.setRatings(row, movie);
                    break;
                case "Режиссер:":
                    MovieDataParser.setDirector(row, movie);
                    break;
                case "Актеры:":
                    MovieDataParser.setActors(row, movie);
                    break;
                default:
            }
        }

        MovieDataParser.setPosterLink(document, movie);
        MovieDataParser.setTrailerLink(movie);

        if (movie.getTitle()!=null) {
            movieRepository.save(movie);
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }
}
