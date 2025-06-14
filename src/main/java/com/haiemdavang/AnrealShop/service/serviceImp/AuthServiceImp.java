package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.auth.LoginRequest;
import com.haiemdavang.AnrealShop.dto.auth.LoginResponse;
import com.haiemdavang.AnrealShop.security.jwt.JwtInit;
import com.haiemdavang.AnrealShop.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtInit jwtInit;

    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseCookie accessTokenCookie = jwtInit.generaJwtCookie(authentication);
        ResponseCookie refreshTokenCookie = jwtInit.generaJwtRefreshCookie(authentication);

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return new LoginResponse(
                accessTokenCookie.getValue(),
                refreshTokenCookie.getValue()
        );
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtInit.getTokenFromCookie(request);
        jwtInit.deleteToken(token);

        ResponseCookie accessTokenCookie = jwtInit.getCleanJwtCookie();
        ResponseCookie refreshTokenCookie = jwtInit.getCleanJwtRefreshCookie();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
