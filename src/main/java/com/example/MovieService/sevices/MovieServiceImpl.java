package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.MovieDto;
import com.example.MovieService.models.dtos.movieDtos.MovieFilterDTO;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.custom.CustomMovieRepositoryImpl;
import com.example.MovieService.sevices.interfaces.MovieService;
import com.example.MovieService.utils.mappers.MovieMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository repository;
    private final CustomMovieRepositoryImpl customRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository repository, CustomMovieRepositoryImpl customRepository) {
        this.repository = repository;
        this.customRepository = customRepository;
    }

    @Override
    public List<Movie> findAllMovies(){
        log.info("Return all movies");
        return repository.findAll();
    }

    @Override
    public List<MovieDto> filteredMovies(MovieFilterDTO dto) {
        return customRepository.findMovies(dto);
    }

    @Override
    public List<MovieDto> findAllMovieDto(Sort sorting) {
        log.info("Retrieving all movies from the database");
        return repository.findAll(sorting).stream()
                .map(MovieMapper::mapToMovieDto)
                .collect(Collectors.toList());
    }

    @Override
    public Movie findMovieById(long id) {
        log.info("Searching for movie with ID: {}", id);
        return Objects.requireNonNull(repository.findById(id), "Movie cannot be null");
    }

    @Override
    public void saveMovie(@Valid Movie movie) {
        log.info("Saving movie: {}", movie.getTitle());
        repository.save(Objects.requireNonNull(movie, "Movie cannot be null."));
    }

    @Override
    public List<MovieDto> findMovieByTitle(String title, Sort sorting) {
        log.info("Searching movies by title containing: {}", title);
        return repository.search(title, sorting).stream()
                .map(MovieMapper::mapToMovieDto)
                .collect(Collectors.toList());
    }
}
