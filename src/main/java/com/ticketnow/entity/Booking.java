package com.ticketnow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "movie_name")
    private String movieName;

    @Column(name = "theatre_name")
    private String theatreName;

    @Column(name="show_time")
    private LocalDateTime showTime;


    @Column(name = "screening_id", nullable = false)
    private Long screeningId;

    @Column(name = "seats_booked", nullable = false)
    private int seatCount;

    @Column(name = "seats", nullable = false)
    private String seats;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt = LocalDateTime.now();

    @Column(name="qr_code", columnDefinition = "TEXT")
    private String qrCode;

    public Long getId() { return id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public String getTheatreName() { return theatreName; }
    public void setTheatreName(String theatreName) { this.theatreName = theatreName; }

    public LocalDateTime getShowTime() { return showTime; }
    public void setShowTime(LocalDateTime showTime) { this.showTime = showTime; }


    public Long getScreeningId() { return screeningId; }
    public void setScreeningId(Long screeningId) { this.screeningId = screeningId; }

    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }

    public String getSeats() { return seats; }
    public void setSeats(String seats) { this.seats = seats; }

    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

}
