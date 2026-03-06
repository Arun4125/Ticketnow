package com.ticketnow.controller;

import com.ticketnow.entity.Theatre;
import com.ticketnow.service.TheatreService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@CrossOrigin
public class TheatreController {

    private final TheatreService service;

    public TheatreController(TheatreService service) {
        this.service = service;
    }

    
    @GetMapping
    public List<Theatre> getAll() {
        return service.getAll();
    }

    
    @GetMapping("/by-movie/{movieId}")  
    public List<Theatre> theatresByMovie(@PathVariable Long movieId) {
        return service.getTheatresByMovie(movieId);
    }
}
