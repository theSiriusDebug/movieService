package com.example.MovieService.sevices;

import com.example.MovieService.MovieServiceApplication;
import com.example.MovieService.models.Movie;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.sevices.interfaces.MovieService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private static final Logger logger = Logger.getLogger(MovieServiceImpl.class.getName());

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAllMovies(){
        return movieRepository.findAll();
    }

    @Override
    public Movie findMovieById(long id) {
        logger.info("Find movie by id " + id);
        return movieRepository.findById(id);
    }

    @Override
    public Movie findOptionalMovieById(long id) {
        try {
            return Optional.ofNullable(movieRepository.findById(id))
                    .orElseThrow(() -> new NotFoundException("Movie not found"));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMovie(Movie movie) {
        movieRepository.delete(Objects.requireNonNull(movie, "Movie cannot be null."));
    }

    @Override
    public void saveMovie(Movie movie) {
        logger.info("Movie saved.");
        movieRepository.save(movie);
    }
}
