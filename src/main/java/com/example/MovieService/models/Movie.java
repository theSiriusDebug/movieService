package com.example.MovieService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private String release_year;
    private String director;

    @ManyToMany
    @JoinTable(name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genres_id"))
    private List<Genre> genres;

    private String duration;
    private double rating;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "horizontal_image")
    private String horizontal_image;
    @Column(name = "video")
    private String video;
    @Transient
    private MultipartFile coverImageFile;
    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Review> reviews;

    private String trailer;
}
