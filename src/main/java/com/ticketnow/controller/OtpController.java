package com.ticketnow.controller;

import com.ticketnow.dto.SendOtpRequest;
import com.ticketnow.dto.VerifyOtpRequest;
import com.ticketnow.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody SendOtpRequest request) {
        otpService.sendOtp(request.getPhone());
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        otpService.verifyOtp(request.getPhone(), request.getOtp());
        return ResponseEntity.ok("OTP verified, booking confirmed");
    }
}

