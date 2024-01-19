package com.example.MovieService.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private String title;
    private int year;
    private String quality;
    private String language;
    private String duration;
    private String country;
    private List<String> genres = new ArrayList<>();
    private double imdbRating;
    private double kinopoiskRating;
    private String posterLink;
    private int reviewsCount;
}
