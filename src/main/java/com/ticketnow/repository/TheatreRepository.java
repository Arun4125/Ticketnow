package com.ticketnow.repository;

import com.ticketnow.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TheatreRepository extends JpaRepository<Theatre, Long> {

    @Query(
      value = "SELECT DISTINCT t.* " +
              "FROM theatre t " +
              "JOIN screening s ON s.theatre_id = t.id " +
              "WHERE s.movie_id = :movieId",
      nativeQuery = true
    )
    List<Theatre> findTheatresByMovie(@Param("movieId") Long movieId);
}
