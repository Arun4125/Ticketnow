package com.ticketnow.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.ticketnow.repository.BookingRepository;
import com.ticketnow.repository.SeatRepository;
import com.ticketnow.dto.BookingRequest;
import com.ticketnow.entity.Booking;
import com.ticketnow.entity.Seat;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.util.stream.Collectors;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import com.ticketnow.entity.Movie;
import com.ticketnow.entity.Theatre;
import com.ticketnow.entity.Screening;
import com.ticketnow.repository.MovieRepository;
import com.ticketnow.repository.TheatreRepository;
import com.ticketnow.repository.ScreeningRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final SeatRepository seatRepo;
    private final ScreeningRepository screeningRepo;
    private final MovieRepository movieRepo;
    private final TheatreRepository theatreRepo;

    public BookingService(BookingRepository bookingRepo, SeatRepository seatRepo,
                          ScreeningRepository screeningRepo,
                          MovieRepository movieRepo,
                          TheatreRepository theatreRepo) {
        this.bookingRepo = bookingRepo;
        this.seatRepo = seatRepo;
        this.screeningRepo = screeningRepo;
        this.movieRepo = movieRepo;
        this.theatreRepo = theatreRepo;
    }
@Transactional
public Booking book(BookingRequest req) {

   
    Screening scr = screeningRepo.findById(req.getScreeningId())
            .orElseThrow(() -> new RuntimeException("Screening not found"));

   
    for (String code : req.getSeats()) {

        String row = code.substring(0, 1); 
        String col = code.substring(1);  



        Seat seat = seatRepo
                .findByScreeningIdAndSeatRowAndSeatCol(req.getScreeningId(), row, col)
                .orElseThrow(() -> new RuntimeException("Seat not found: " + code));

        if (seat.isBooked()) {
            throw new RuntimeException("Seat already booked: " + code);
        }

        seat.setBooked(true);
        seatRepo.save(seat);
    }

    
    Booking booking = new Booking();
    booking.setCustomerName(req.getCustomerName());
    booking.setScreeningId(req.getScreeningId());
    booking.setSeatCount(req.getSeats().size());
    booking.setSeats(String.join(",", req.getSeats()));
    booking.setBookedAt(LocalDateTime.now());

    Booking saved = bookingRepo.save(booking);

    saved.setMovieName(scr.getMovie().getTitle());
    saved.setTheatreName(scr.getTheatre().getName());
    saved.setShowTime(scr.getShowTime());

    
    String qrText = "Booking ID: " + saved.getId() +
            "\nName: " + saved.getCustomerName() +
            "\nSeats: " + saved.getSeats() +
            "\nMovie: " + saved.getMovieName() +
            "\nTheatre: " + saved.getTheatreName() +
            "\nShow Time: " + saved.getShowTime();

    saved.setQrCode(generateQRCodeBase64(qrText));

    return bookingRepo.save(saved);
}

public Booking getBooking(Long id) {
    Booking booking = bookingRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

    Screening screening = screeningRepo.findById(booking.getScreeningId())
            .orElseThrow(() -> new RuntimeException("Screening not found"));

    booking.setMovieName(screening.getMovie().getTitle());
    booking.setTheatreName(screening.getTheatre().getName());

    return booking;
}


    private String generateQRCodeBase64(String text){
        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix matrix = qrWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("QR code generation failed", e);
        }
    }
    public List<Booking> getAllBookings(){
    List<Booking> list = bookingRepo.findAll();

    for(Booking b : list){
        Screening s = screeningRepo.findById(b.getScreeningId()).orElse(null);
        if(s != null){
            b.setMovieName(s.getMovie().getTitle());
            b.setTheatreName(s.getTheatre().getName());
            b.setShowTime(s.getShowTime());
        }
    }
    return list;
}

}
