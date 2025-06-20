package com.haiemdavang.AnrealShop.service.auth;

import com.haiemdavang.AnrealShop.dto.auth.LoginRequest;
import com.haiemdavang.AnrealShop.dto.auth.LoginResponse;
import com.haiemdavang.AnrealShop.dto.user.UserDto;
import com.haiemdavang.AnrealShop.security.jwt.JwtInit;
import com.haiemdavang.AnrealShop.service.IAuthService;
import com.haiemdavang.AnrealShop.service.IUserService;
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
    private final IUserService userService;

    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseCookie accessTokenCookie = jwtInit.generaJwtCookie(authentication);
        ResponseCookie refreshTokenCookie = jwtInit.generaJwtRefreshCookie(authentication);

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        UserDto user = userService.findDtoByEmail(loginRequest.getUsername());

        return new LoginResponse(accessTokenCookie.getValue(), user);
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

    @Override
    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtInit.getTokenRefreshFromCookie(request);
        jwtInit.deleteToken(token);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        ResponseCookie newToken = jwtInit.generaJwtCookie(auth);
        ResponseCookie newTokenRefresh = jwtInit.generaJwtRefreshCookie(auth);

        response.addHeader("Set-Cookie", newToken.toString());
        response.addHeader("Set-Cookie", newTokenRefresh.toString());

        return new LoginResponse(newToken.getValue(), null);
    }




}
