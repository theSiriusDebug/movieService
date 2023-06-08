package com.example.MovieService.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "movies")
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private String release_year;
    private String director;
    private String genre;
    private String actors;
    private String duration;
    private double rating;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "video")
    private String video;

    public Movie(long id, String title, String description, String releaseYear  , String director, String genre, String actors, String duration, double rating, String coverImage, String video) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.release_year = releaseYear;
        this.director = director;
        this.genre = genre;
        this.actors = actors;
        this.duration = duration;
        this.rating = rating;
        this.coverImage = coverImage;
        this.video = video;
    }
    public Movie(){}
}
