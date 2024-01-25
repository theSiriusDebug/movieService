package com.example.MovieService.utils.validator;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.MovieDetailsDto;

import java.util.stream.Collectors;

public class MovieDetailsMapper {
    public static MovieDetailsDto mapToMovieDetailsDto(Movie movie){
        return new MovieDetailsDto(
                movie.getTitle(),
                movie.getYear(),
                movie.getQuality(),
                movie.getLanguage(),
                movie.getDuration(),
                movie.getCountry(),
                movie.getGenres(),
                movie.getImdbRating(),
                movie.getKinopoiskRating(),
                movie.getDirector(),
                movie.getPosterLink(),
                movie.getTrailerLink(),
                movie.getMovieLink(),
                movie.getActors(),
                movie.getReviews().stream()
                        .map(ReviewMapper::mapToReviewDto)
                        .collect(Collectors.toList())
        );
    }
}
