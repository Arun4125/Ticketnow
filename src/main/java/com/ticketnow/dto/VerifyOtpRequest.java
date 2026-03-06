package com.ticketnow.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String phone;
    private String otp;
}

