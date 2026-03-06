package com.ticketnow.service;

import com.ticketnow.entity.Seat;
import com.ticketnow.entity.Screening;
import com.ticketnow.repository.SeatRepository;
import com.ticketnow.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;

    public SeatService(SeatRepository seatRepository, ScreeningRepository screeningRepository) {
        this.seatRepository = seatRepository;
        this.screeningRepository = screeningRepository;
    }

    public List<Seat> getSeats(Long screeningId) {      

        List<Seat> seats = seatRepository.findByScreeningId(screeningId);

        if (seats.isEmpty()) {
            Screening screening = screeningRepository.findById(screeningId)
                    .orElseThrow(() -> new RuntimeException("Screening not found"));

            seats = new ArrayList<>();
            int created = 0;
            for (char r = 'A'; r <= 'N' && created < 299; r++) {  
                for (char c = 'A'; c <= 'Z' && created < 299; c++) { 
                    Seat seat = new Seat();
                    seat.setScreeningId(screening.getId());
                    seat.setSeatRow(String.valueOf(r));
                    seat.setSeatCol(String.valueOf(c));
                    seat.setBooked(false);
                    seats.add(seat);
                    created++;
                }             
            }
            seatRepository.saveAll(seats);
        }          

        return seats;
    }

    
    public boolean lockSeat(Long seatId) {
        int updated = seatRepository.lockSeat(seatId);
        return updated > 0;
    }

    
    public int availableSeats(Long screeningId) {
        return seatRepository.countAvailable(screeningId);
    }
}
