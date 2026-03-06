package com.ticketnow.service;

import com.ticketnow.entity.OtpVerification;
import com.ticketnow.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private SmsService smsService;

    public void sendOtp(String phone) {

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        OtpVerification entity = new OtpVerification();
        entity.setPhone(phone);
        entity.setOtp(otp);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        entity.setVerified(false);

        otpRepository.save(entity);
        //smsService.sendOtp(phone, otp);
        System.out.println("OTP for " + phone + " is: " + otp);
    }

    public boolean verifyOtp(String phone, String otp) {

        OtpVerification savedOtp = otpRepository
                .findTopByPhoneOrderByIdDesc(phone)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (savedOtp.isVerified())
            throw new RuntimeException("OTP already used");

        if (savedOtp.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        if (!savedOtp.getOtp().equals(otp))
            throw new RuntimeException("Invalid OTP");

        savedOtp.setVerified(true);
        otpRepository.save(savedOtp);

        return true;
    }
}
