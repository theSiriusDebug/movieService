package com.example.MovieService.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailsDto {
    private String title;
    private int year;
    private String quality;
    private String language;
    private String duration;
    private String country;
    private List<String> genres;
    private double imdbRating;
    private double kinopoiskRating;
    private String director;
    private String posterLink;
    private String trailerLink;
    private String movieLink;
    private List<String> actors;
    private List<ReviewDto> reviews;
}
