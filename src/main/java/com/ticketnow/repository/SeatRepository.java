package com.ticketnow.repository;

import com.ticketnow.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    
    List<Seat> findByScreeningId(Long screeningId);

    
    Optional<Seat> findByScreeningIdAndSeatRowAndSeatCol(Long screeningId, String seatRow, String seatCol);

    
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.screeningId = :id AND s.booked = false")
    int countAvailable(@Param("id") Long screeningId);

    
    @Modifying
    @Transactional
    @Query("UPDATE Seat s SET s.booked = true WHERE s.id = :seatId AND s.booked = false")
    int lockSeat(@Param("seatId") Long seatId);
}
