package com.ticketnow.service;

import com.ticketnow.entity.Booking;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class TicketPdfService {

    public byte[] generateTicket(Booking b) {

       
        String qrBase64 = "";
        try {
            com.google.zxing.qrcode.QRCodeWriter qrCodeWriter = new com.google.zxing.qrcode.QRCodeWriter();
            com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(
                    "Booking ID: " + b.getId(),
                    com.google.zxing.BarcodeFormat.QR_CODE,
                    150, 150
            );

            ByteArrayOutputStream pngOutput = new ByteArrayOutputStream();
            com.google.zxing.client.j2se.MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutput);
            qrBase64 = java.util.Base64.getEncoder().encodeToString(pngOutput.toByteArray());
        } catch (Exception e) {
            qrBase64 = ""; 
        }

       
        String html = """
        <html>
        <head>
            <style>
                body { font-family: Arial; background:#f5f5f5; }
                .ticket {
                    width: 400px;
                    background: white;
                    margin: auto;
                    padding: 20px;
                    border-radius: 12px;
                    border: 2px dashed #0d0c0b;
                }
                h2 { text-align:center; color:#ff9800; }
                .row { margin: 8px 0; }
                .label { font-weight:bold; }
                .qr { text-align:center; margin-top:15px; }
            </style>
        </head>
        <body>
            <div class="ticket">
                <h2>TICKET NOW</h2>
                <div class="row"><span class="label">Name:</span> %s</div>
                <div class="row"><span class="label">Movie:</span> %s</div>
                <div class="row"><span class="label">Theatre:</span> %s</div>
                <div class="row"><span class="label">Show Time:</span> %s</div>
                <div class="row"><span class="label">Seats:</span> %s</div>
                <div class="row"><span class="label">Booking ID:</span> %d</div>
                <div class="qr">
                    <img src="data:image/png;base64,%s" alt="QR Code" style="margin-top:10px;"/>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                b.getCustomerName(),
                b.getMovieName(),
                b.getTheatreName(),
                b.getShowTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")),
                b.getSeats(),
                b.getId(),
                qrBase64
        );

        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation error", e);
        }

        return os.toByteArray();
    }
}

