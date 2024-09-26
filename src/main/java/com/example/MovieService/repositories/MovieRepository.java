package com.example.MovieService.repositories;

import com.example.MovieService.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findById(long id);
    Optional<Movie> findById(Long id);
}
