package com.ticketnow.service;

import com.ticketnow.entity.Theatre;
import com.ticketnow.repository.TheatreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    private final TheatreRepository repo;

    public TheatreService(TheatreRepository repo) {
        this.repo = repo;
    }

    
    public List<Theatre> getAll() {
        return repo.findAll();
    }

    public List<Theatre> getTheatresByMovie(Long movieId){
        return repo.findTheatresByMovie(movieId);
    }
}
