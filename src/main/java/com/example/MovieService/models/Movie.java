package com.example.MovieService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter @Setter
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
    @Transient
    private MultipartFile coverImageFile;
    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Review> reviews;

    private String trailer;

    public Movie(String title, String description, String release_year, String director, String genre, String actors, String duration, double rating, String coverImage, String video) {
        this.title = title;
        this.description = description;
        this.release_year = release_year;
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
