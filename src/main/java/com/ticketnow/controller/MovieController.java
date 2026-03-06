package com.ticketnow.controller;

import com.ticketnow.entity.Movie;
import com.ticketnow.entity.Theatre;
import com.ticketnow.service.MovieService;
import com.ticketnow.service.TheatreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin
public class MovieController {

    private final MovieService movieService;
    private final TheatreService theatreService;

    public MovieController(MovieService movieService, TheatreService theatreService) {
        this.movieService = movieService;
        this.theatreService = theatreService;
    }

    @GetMapping
    public List<Movie> getMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    @GetMapping("/{id}/theatres")
    public List<Theatre> getTheatres(@PathVariable Long id){
        return theatreService.getTheatresByMovie(id);
    }
}
