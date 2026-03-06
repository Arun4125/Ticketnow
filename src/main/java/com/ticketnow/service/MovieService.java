package com.ticketnow.service;

import com.ticketnow.entity.Movie;
import com.ticketnow.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository repo;

    public MovieService(MovieRepository repo) {
        this.repo = repo;
    }

    public List<Movie> getAllMovies() {
        return repo.findAll();
    }

    public Movie getMovie(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
