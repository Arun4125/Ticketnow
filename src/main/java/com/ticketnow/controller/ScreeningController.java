package com.ticketnow.controller;

import com.ticketnow.entity.Screening;
import com.ticketnow.service.ScreeningService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ScreeningController {

    private final ScreeningService service;

    public ScreeningController(ScreeningService service) {
        this.service = service;
    }

    @GetMapping("/screenings")
    public List<Screening> getScreenings(@RequestParam Long movieId,
                                         @RequestParam Long theatreId){
        return service.findSlots(movieId, theatreId);
    }
    @GetMapping("/screenings/available/{id}")
public int availableSeats(@PathVariable Long id) {
    return service.countAvailableSeats(id);
}

}
