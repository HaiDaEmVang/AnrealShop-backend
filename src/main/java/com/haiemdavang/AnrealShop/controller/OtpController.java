package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.auth.OtpRequest;
import com.haiemdavang.AnrealShop.mail.MailType;
import com.haiemdavang.AnrealShop.mail.service.IMailService;
import io.jsonwebtoken.lang.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    private final IMailService mailService;
    @PostMapping("/sendOtp/{email}")
    public ResponseEntity<?> getCode(@PathVariable String email, @RequestParam String type){
        mailService.sendOTP(email, MailType.valueOf(type.toUpperCase()));
        return ResponseEntity.ok(Maps.of("message", "Gửi email thành công!"));
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<?> verifyOTP(@RequestBody OtpRequest otpObject){
        mailService.verifyOTP(otpObject.code(), otpObject.email());
        return ResponseEntity.ok(Maps.of("message", "Xác thực thành otp cho email "+ otpObject.email()+" Thành công!!"));
    }
}
