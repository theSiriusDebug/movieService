package com.example.MovieService.models.api_Items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieApiItem {
    private String title;
    private String overview;
    private String release_date;
    private String director;
    private List<Long> genre_ids;
    private String runtime;
    private double vote_average;
    private String poster_path;
    private String video_path;
    private String trailer_path;
}
