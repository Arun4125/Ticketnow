package com.ticketnow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="screening")
public class Screening {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name="movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="theatre_id", nullable = false)
    private Theatre theatre;

    @Column(name="show_time")
    private LocalDateTime showTime;

    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public Theatre getTheatre() { return theatre; }
    public void setTheatre(Theatre theatre) { this.theatre = theatre; }

    public LocalDateTime getShowTime() { return showTime; }
    public void setShowTime(LocalDateTime showTime) { this.showTime = showTime; }

    
}
