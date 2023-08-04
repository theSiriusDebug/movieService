package com.example.MovieService.TEST;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieApiResponse {
    private List<MovieApiItem> results;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class MovieApiItem {
    private String title;
    private String overview;
    private String release_date;
    private String director;
    private List<String> genres;
    private List<String> actors;
    private String runtime;
    private double vote_average;
    private String poster_path;
    private String video_path;
    private String trailer_path;
}
