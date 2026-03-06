package com.ticketnow.repository;

import com.ticketnow.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    @Query("SELECT s FROM Screening s " +
           "WHERE s.movie.id = :movieId AND s.theatre.id = :theatreId")
    List<Screening> findByMovieAndTheatre(@Param("movieId") Long movieId,
                                          @Param("theatreId") Long theatreId);
}
