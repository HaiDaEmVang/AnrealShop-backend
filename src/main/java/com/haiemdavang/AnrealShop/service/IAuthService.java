package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.auth.LoginRequest;
import com.haiemdavang.AnrealShop.dto.auth.TokenResponse;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {
    TokenResponse login(LoginRequest loginRequest, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    TokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    TokenResponse oauthLogin(String provider, String code, HttpServletRequest request, HttpServletResponse response);

}
