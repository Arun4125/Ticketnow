package com.ticketnow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "screening_id", nullable = false)
    private Long screeningId;

    @Column(name = "seat_row")
    private String seatRow;

    @Column(name = "seat_col")
    private String seatCol;

    @Column(name = "is_booked")
    private boolean booked = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getScreeningId() { return screeningId; }
    public void setScreeningId(Long screeningId) { this.screeningId = screeningId; }

    public String getSeatRow() { return seatRow; }
    public void setSeatRow(String seatRow) { this.seatRow = seatRow; }

    public String getSeatCol() { return seatCol; }
    public void setSeatCol(String seatCol) { this.seatCol = seatCol; }

    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }
}
