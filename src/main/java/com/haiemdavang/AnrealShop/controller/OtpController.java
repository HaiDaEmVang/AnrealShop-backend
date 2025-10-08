package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.auth.OtpRequest;
import com.haiemdavang.AnrealShop.tech.mail.service.IMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    private final IMailService mailService;
    @PostMapping("/sendOtp/{email}")
    public ResponseEntity<?> getCode(@PathVariable String email, @RequestParam String type){
        mailService.sendOTP(email, type);
        return ResponseEntity.ok(Map.of("message", "Gửi email thành công!"));
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<?> verifyOTP(@RequestBody OtpRequest otpObject){
        mailService.verifyOTP(otpObject.code(), otpObject.email());
        return ResponseEntity.ok(Map.of("message", "Xác thực otp cho email "+ otpObject.email()+" Thành công!!"));
    }
}
