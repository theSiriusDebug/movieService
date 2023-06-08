package com.example.MovieService.repositories;

import com.example.MovieService.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findById(long id);
    List<Movie> findByTitleStartingWithIgnoreCase(String title);
}
