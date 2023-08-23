package com.example.MovieService.controllers.TEST;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
public class parser {
    private static filmRepository filmRepository;
    static OkHttpClient client = new OkHttpClient();

    @Autowired
    public parser(filmRepository filmRepository) {
        this.filmRepository = filmRepository;
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

        film movie = new film();
        Map<String, Consumer<Element>> fieldActions = new HashMap<>();
        fieldActions.put("Оригинал:", row -> {
            String originalTitle = row.select("td:nth-child(2)").text();
            movie.setOriginalTitle(originalTitle);
            System.out.println(originalTitle);
        });
        fieldActions.put("Год:", row -> {
            int year = Integer.parseInt(row.select("td:nth-child(2)").text());

            movie.setYear(year);
            System.out.println(year);
        });
        fieldActions.put("Качество:", row -> {
            String quality = String.valueOf(row.select("td:nth-child(2)").text());
            movie.setQuality(quality);
            System.out.println(quality);
        });
        fieldActions.put("Перевод:", row -> {
            String language = String.valueOf(row.select("td:nth-child(2)").text());
            movie.setLanguage(language);
            System.out.println(language);
        });
        fieldActions.put("Время:", row -> {
            String duration = String.valueOf(row.select("td:nth-child(2)").text());
            movie.setDuration(duration);
            System.out.println(duration);
        });
        fieldActions.put("Страна:", row -> {
            String country = String.valueOf(row.select("td:nth-child(2)").text());
            movie.setCountry(country);
            System.out.println(country);
        });
        fieldActions.put("Жанр:", row -> {
            String genre = String.valueOf(row.select("td:nth-child(2)").text());
            movie.setGenre(genre);
            System.out.println(genre);
        });

        fieldActions.put("Рейтинг:", row -> {
            Elements ratingElements = row.select("span[style*=color]");
            String imdbRating = ratingElements.get(0).text();
            String kinopoiskRating = ratingElements.get(1).text();
            movie.setImdbRating(Double.parseDouble(imdbRating));
            movie.setKinopoiskRating(Double.parseDouble(kinopoiskRating));
            System.out.println("IMDb Rating: " + imdbRating);
            System.out.println("Kinopoisk Rating: " + kinopoiskRating);
        });
        fieldActions.put("Режиссер:", row -> {
            String director = row.select("td:nth-child(2)").text();
            movie.setDirector(director); // Сохраняем режиссера
            System.out.println("Director: " + director);
        });

        List<String> actorsList = new ArrayList<>(); // Создаем список для актеров
        fieldActions.put("Актеры:", row -> {
            String actors = row.select("td:nth-child(2)").text();
            String[] actorNames = actors.split(", ");
            for (String actorName : actorNames) {
                movie.getActors().add(actorName); // Добавляем имя актера в список
                System.out.println("Actor: " + actorName);
            }
        });

        for (Element row : movieRows) {
            String fieldName = row.select("td:nth-child(1)").text();
            if (fieldActions.containsKey(fieldName)) {
                fieldActions.get(fieldName).accept(row);
            }
        }

        filmRepository.save(movie);
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    static void printField(String label, String value) {
        System.out.println(label + ": " + value);
    }

    @PostMapping("/parserasd")
    public static void main(String[] args) {
        long latestPage = 10;
        long currentPage = 1;
        boolean switcher = true;

        while (switcher) {
            try {
                String html = run(String.format("https://mw.anwap.tube/films/%d", currentPage));
                parseMovieDetails(html);
                System.out.println("Successfully!");
            } catch (IOException e) {
                System.out.println("Error!");
                throw new RuntimeException(e);
            }
            if (currentPage == latestPage) {
                break;
            }
            currentPage++;
        }
    }

    @GetMapping("/geewq")
    public List<film> get() {
        return filmRepository.findAll();
    }
    @GetMapping("/del")
    public String del(){
        filmRepository.deleteAll();
        return "Hi!";
    }
}