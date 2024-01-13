package com.example.MovieService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    private Movie movie;

    @OneToMany(mappedBy = "parentReview", cascade = CascadeType.ALL)
    private List<Reply> replies;

    @NotBlank(message = "reviewText must not be blank")
    @Min(value = 1)
    private String reviewText;

    @Transient
    private String reviewOwner;

    public String getReviewOwner() {
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }
}