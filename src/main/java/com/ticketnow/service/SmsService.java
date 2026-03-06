package com.ticketnow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

    @Value("${fast2sms.api.key}")
    private String apiKey;


    @Autowired
    private RestTemplate restTemplate;

    public void sendOtp(String phone, String otp) {
    String url = "https://www.fast2sms.com/dev/bulk";

    HttpHeaders headers = new HttpHeaders();
    headers.set("authorization", apiKey);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("route", "p");                     // transactional OTP
    body.add("numbers", "91" + phone);          // prepend country code
    body.add("message", "Your OTP is: " + otp);
    body.add("language", "english");
    body.add("flash", "0");
    body.add("sender_id", "TXTIND");            // approved sender ID

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    try {
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        String resp = response.getBody();
        if (resp != null && resp.contains("\"return\":true")) {
            System.out.println("OTP sent successfully to " + phone);
        } else {
            System.err.println("Failed to send OTP: " + resp);
        }
    } catch (Exception e) {
        System.err.println("Failed to send SMS: " + e.getMessage());
    }
}
}