package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.common.ResponseDto;
import com.haiemdavang.AnrealShop.dto.auth.OtpRequest;
import com.haiemdavang.AnrealShop.mail.service.IMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    private final IMailService mailService;
    @PostMapping("/sendOtp/{email}")
    public ResponseEntity<ResponseDto<String>> getCode(@PathVariable String email){
        mailService.sendOTP(email);
        return ResponseEntity.ok(ResponseDto.<String>builder().data("Gửi email thành công!").build());
    }


    @PostMapping("/verifyOTP")
    public ResponseEntity<ResponseDto<String>> verifyOTP(@RequestBody OtpRequest otpObject){
        mailService.verifyOTP(otpObject.code(), otpObject.email());
        return ResponseEntity.ok(ResponseDto.<String>builder().data("Xác thực thành otp cho email "+ otpObject.email()+" Thành công!!").build());
    }
}
