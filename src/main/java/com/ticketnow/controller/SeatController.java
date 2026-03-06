package com.ticketnow.controller;

import com.ticketnow.entity.Seat;
import com.ticketnow.service.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/{screeningId}")
    public List<Seat> getSeats(@PathVariable Long screeningId) {
        return seatService.getSeats(screeningId);
    }
}
