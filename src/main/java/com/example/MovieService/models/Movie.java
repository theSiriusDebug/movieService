package com.example.MovieService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
