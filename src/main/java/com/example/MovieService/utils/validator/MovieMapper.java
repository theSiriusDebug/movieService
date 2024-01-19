package com.example.MovieService.utils.validator;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.MovieDto;

public class MovieMapper {
    public static MovieDto mapToMovieDto(Movie movie){
        return new MovieDto(
                movie.getTitle(),
                movie.getYear(),
                movie.getQuality(),
                movie.getLanguage(),
                movie.getDuration(),
                movie.getCountry(),
                movie.getGenres(),
                movie.getImdbRating(),
                movie.getKinopoiskRating(),
                movie.getPosterLink(),
                movie.getReviews().size()
        );
    }
}
