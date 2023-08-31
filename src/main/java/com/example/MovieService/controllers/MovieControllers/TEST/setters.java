package com.example.MovieService.controllers.MovieControllers.TEST;

import com.example.MovieService.models.Movie;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
public class setters {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TD_SELECTOR = "td:nth-child(2)";
    private static final Logger logger = LoggerFactory.getLogger(setters.class);

    public static void setOriginalTitle(Element row, Movie movie) {
        String originalTitle = row.select(TD_SELECTOR).text();
        movie.setOriginalTitle(originalTitle);
        logger.info("Original Title set: {}", originalTitle);
    }

    public static void setYear(Element row, Movie movie) {
        int year = Integer.parseInt(row.select(TD_SELECTOR).text());
        movie.setYear(year);
        logger.info("Year set: {}", year);
    }

    public static void setQuality(Element row, Movie movie) {
        String quality = String.valueOf(row.select(TD_SELECTOR).text());
        movie.setQuality(quality);
        logger.info("Quality set: {}", quality);
    }

    public static void setLanguage(Element row, Movie movie) {
        String language = String.valueOf(row.select(TD_SELECTOR).text());
        movie.setLanguage(language);
        logger.info("Language set: {}", language);
    }

    public static void setDuration(Element row, Movie movie) {
        String duration = String.valueOf(row.select(TD_SELECTOR).text());
        movie.setDuration(duration);
        logger.info("Duration set: {}", duration);
    }

    public static void setCountry(Element row, Movie movie) {
        String country = String.valueOf(row.select(TD_SELECTOR).text());
        movie.setCountry(country);
        logger.info("Country set: {}", country);
    }

    public static void setGenres(Element row, Movie movie) {
        String genre = row.select(TD_SELECTOR).text();
        String[] genreNames = genre.split(" ");
        for (String genreName : genreNames) {
            movie.getGenres().add(genreName);
            logger.info("Genre added: {}", genreNames);
        }
    }

    public static void setRatings(Element row, Movie movie) {
        Elements ratingElements = row.select("span[style*=color]");
        String imdbRating = ratingElements.get(0).text();
        String kinopoiskRating = ratingElements.get(1).text();
        movie.setImdbRating(Double.parseDouble(imdbRating));
        movie.setKinopoiskRating(Double.parseDouble(kinopoiskRating));
        logger.info("IMDb Rating set: {}", imdbRating);
        logger.info("Kinopoisk Rating set: {}", kinopoiskRating);
    }

    public static void setDirector(Element row, Movie movie) {
        String director = row.select(TD_SELECTOR).text();
        movie.setDirector(director);
        logger.info("Director set: {}", director);
    }

    public static void setTrailerLink(Movie movie) {
        try {
            String trailerLink = findTrailer(movie.getOriginalTitle());
            logger.info("Trailer Link set: {}", trailerLink);
            movie.setTrailerLink(trailerLink);
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Failed to set trailer link for movie: {}", movie.getOriginalTitle(), e);
            throw new RuntimeException("Failed to set trailer link for movie: " + movie.getOriginalTitle(), e);
        }
    }

    public static void setActors(Element row, Movie movie) {
        String actors = row.select(TD_SELECTOR).text();
        String[] actorNames = actors.split(", ");
        for (String actorName : actorNames) {
            movie.getActors().add(actorName);
            logger.info("Actor added: {}", actorName);
        }
    }

    public static String findTrailer(String query) throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        YouTube youtubeService = buildYouTubeService(httpTransport);

        YouTube.Search.List searchRequest = buildSearchRequest(youtubeService, query);
        SearchListResponse searchResponse = executeSearchRequest(searchRequest);
        String url = extractTrailerUrl(searchResponse);

        return url;
    }

    private static YouTube buildYouTubeService(NetHttpTransport httpTransport) throws IOException {
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setGoogleClientRequestInitializer(request -> request.setDisableGZipContent(true))
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer("AIzaSyB1ueY-KvwDKhs6tLqtrULVrZww7SYauIo"))
                .setApplicationName("movieService") // Установите имя вашего приложения здесь
                .build();
    }

    private static YouTube.Search.List buildSearchRequest(YouTube youtubeService, String query) throws IOException {
        YouTube.Search.List searchRequest = youtubeService.search().list("id,snippet");
        searchRequest.setQ(query + " trailer");
        searchRequest.setType("video");
        searchRequest.setMaxResults(1L);
        return searchRequest;
    }

    private static SearchListResponse executeSearchRequest(YouTube.Search.List searchRequest) throws IOException {
        return searchRequest.execute();
    }

    private static String extractTrailerUrl(SearchListResponse searchResponse) {
        List<SearchResult> searchResults = searchResponse.getItems();
        if (searchResults != null && !searchResults.isEmpty()) {
            SearchResult result = searchResults.get(0);
            String videoId = result.getId().getVideoId();
            return "https://www.youtube.com/watch?v=" + videoId;
        }
        return "";
    }
}
