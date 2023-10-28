package com.example.MovieService.repositories;

import com.example.MovieService.models.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findById(long id);
    Optional<Movie> findById(Long id);
    List<Movie> findByTitleStartingWithIgnoreCase(String title);
    List<Movie>findByTitleContainingIgnoreCase(String title, Sort sorting);
}
