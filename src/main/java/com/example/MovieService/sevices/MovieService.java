package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import javassist.NotFoundException;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private static final Logger logger = Logger.getLogger(MovieService.class.getName());

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    private List<Movie> findAllMovies(){
        return movieRepository.findAll();
    }

    public Movie findMovieById(long id) {
        return movieRepository.findById(id);
    }

    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }
}
