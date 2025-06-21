package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.auth.LoginRequest;
import com.haiemdavang.AnrealShop.dto.auth.LoginResponse;
import com.haiemdavang.AnrealShop.dto.auth.ForgotPwRequest;
import com.haiemdavang.AnrealShop.mail.service.IMailService;
import com.haiemdavang.AnrealShop.service.IAuthService;
import com.haiemdavang.AnrealShop.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final IAuthService authService;
    private final IMailService mailService;
    private final IUserService userService;
    
    @PostMapping("/login")
    public ResponseEntity< LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }


    @PostMapping("auth/refresh-token")
    public ResponseEntity< LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công!"));
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<?> resetPass(@RequestBody @Valid ForgotPwRequest resetPassword){
        mailService.verifyOTP(resetPassword.getOtp(), resetPassword.getEmail());
        userService.resetPassword(resetPassword.getEmail(), resetPassword.getPassword());
        mailService.delOTP(resetPassword.getEmail());
        return ResponseEntity.ok(Map.of("message", "Thay đổi mật khẩu thành công!"));
    }
}
