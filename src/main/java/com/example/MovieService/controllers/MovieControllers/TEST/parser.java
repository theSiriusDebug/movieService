package com.example.MovieService.controllers.MovieControllers.TEST;

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
import static com.example.MovieService.controllers.MovieControllers.TEST.setters.*;

@Component
public class parser {
    private static MovieRepository movieRepository;
    static OkHttpClient client = new OkHttpClient();
    private static setters setters;

    @Autowired
    public parser(MovieRepository movieRepository) {
        parser.movieRepository = movieRepository;

    }

    public static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void parseMovieDetails(String html) {
        Document document = Jsoup.parse(html);
        Elements movieRows = document.select("tr");

        Movie movie = new Movie();

        for (Element row : movieRows) {
            String fieldName = row.select("td:nth-child(1)").text();
            switch (fieldName) {
                case "Оригинал:":
                    setOriginalTitle(row, movie);
                    break;
                case "Год:":
                    setYear(row, movie);
                    break;
                case "Качество:":
                    setQuality(row, movie);
                    break;
                case "Перевод:":
                    setLanguage(row, movie);
                    break;
                case "Время:":
                    setDuration(row, movie);
                    break;
                case "Страна:":
                    setCountry(row, movie);
                    break;
                case "Жанр:":
                    setGenres(row, movie);
                    break;
                case "Рейтинг:":
                    setRatings(row, movie);
                    break;
                case "Режиссер:":
                    setDirector(row, movie);
                    break;
                case "Актеры:":
                    setActors(row, movie);
                    break;
                default:
            }
        }
        setPosterLink(document, movie);
        setTrailerLink(movie);

        movieRepository.save(movie);
        System.out.println("--------------------------------------------------------------------------------------------");
    }
}
