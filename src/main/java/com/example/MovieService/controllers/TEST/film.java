package com.example.MovieService.controllers.TEST;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "f")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalTitle;
    private int year;
    private String quality;
    private String language;
    private String duration;
    private String country;
    private String genre;
    private double imdbRating;
    private double kinopoiskRating;
    private String director;
    private String trailerLink;
    @ElementCollection
    private List<String> actors = new ArrayList<>();

    // Other fields, constructors, getters, setters, and methods
}
