package com.example.MovieService.models.api_Items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieApiResponse {
    private List<MovieApiItem> results;
}
