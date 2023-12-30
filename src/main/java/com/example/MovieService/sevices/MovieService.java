package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.repositories.MovieRepository;
import javassist.NotFoundException;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private static final Logger logger = Logger.getLogger(MovieService.class.getName());

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAllMovies(){
        return movieRepository.findAll();
    }

    public Movie findMovieById(long id) {
        logger.info("Find movie by id " + id);
        return movieRepository.findById(id);
    }

    public Movie findOptionalMovieById(long id) {
        try {
            return Optional.ofNullable(movieRepository.findById(id))
                    .orElseThrow(() -> new NotFoundException("Movie not found"));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }

    public void saveMovie(Movie movie) {
        logger.info("Movie saved.");
        movieRepository.save(movie);
    }
}
