package com.ticketnow.controller;

import com.ticketnow.dto.BookingRequest;
import com.ticketnow.entity.Booking;
import com.ticketnow.service.BookingService;
import com.ticketnow.service.TicketPdfService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    private final BookingService service;
    private final TicketPdfService pdfService;

    public BookingController(BookingService service, TicketPdfService pdfService) {
        this.service = service;
        this.pdfService = pdfService;
    }

    @PostMapping
    public Booking book(@RequestBody BookingRequest req){
        return service.book(req);
    }

    
    @GetMapping("/{id}")
    public Booking summary(@PathVariable Long id){
        return service.getBooking(id);
    }

    
    @GetMapping
    public List<Booking> getAllBookings() {
        return service.getAllBookings();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id){

        Booking booking = service.getBooking(id);
        byte[] pdf = pdfService.generateTicket(booking);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket_"+id+".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
