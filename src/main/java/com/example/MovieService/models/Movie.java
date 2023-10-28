package com.example.MovieService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Data
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
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
    private String posterLink;
    private String trailerLink;
    private String movieLink;
    @ElementCollection
    private List<String> actors = new ArrayList<>();
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();
}

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
}