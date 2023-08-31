package com.example.MovieService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalTitle;
    private int year;
    private String quality;
    private String language;
    private String duration;
    private String country;
    @ElementCollection
    private List<String> genres = new ArrayList<>();
    private double imdbRating;
    private double kinopoiskRating;
    private String director;
    private String trailerLink;
    @ElementCollection
    private List<String> actors = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Review> reviews;
}
