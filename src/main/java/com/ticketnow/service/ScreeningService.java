package com.ticketnow.service;

import com.ticketnow.entity.Screening;
import com.ticketnow.repository.ScreeningRepository;
import com.ticketnow.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreeningService {

    private final ScreeningRepository repo;
    private final SeatRepository seatRepository;
    
    public ScreeningService(ScreeningRepository repo, SeatRepository seatRepository) {
        this.repo = repo;
        this.seatRepository = seatRepository;
    }

    
    public List<Screening> findSlots(Long movieId, Long theatreId){
        return repo.findByMovieAndTheatre(movieId, theatreId);
    }

    
    public int countAvailableSeats(Long screeningId) {
        return seatRepository.countAvailable(screeningId);
    }
}
