package com.haiemdavang.AnrealShop.service.auth;

import com.haiemdavang.AnrealShop.dto.auth.LoginRequest;
import com.haiemdavang.AnrealShop.dto.auth.Oauth2.Oauth2UserInfo;
import com.haiemdavang.AnrealShop.dto.auth.Oauth2.OauthProvider;
import com.haiemdavang.AnrealShop.dto.auth.TokenResponse;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import com.haiemdavang.AnrealShop.exception.AnrealShopException;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
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
    private final Oauth2Service oauth2Service;
    private final IUserService userService;

    public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseCookie accessTokenCookie = jwtInit.generaJwtCookie(authentication);
        ResponseCookie refreshTokenCookie = jwtInit.generaJwtRefreshCookie(authentication);

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return new TokenResponse(
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

    @Override
    public TokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtInit.getTokenRefreshFromCookie(request);
        jwtInit.deleteToken(token);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        ResponseCookie newToken = jwtInit.generaJwtCookie(auth);
        ResponseCookie newTokenRefresh = jwtInit.generaJwtRefreshCookie(auth);

        response.addHeader("Set-Cookie", newToken.toString());
        response.addHeader("Set-Cookie", newTokenRefresh.toString());

        return new TokenResponse(
                newToken.getValue(),
                newTokenRefresh.getValue()
        );
    }

    @Override
    public TokenResponse oauthLogin(String provider, String code, HttpServletRequest request, HttpServletResponse response) {
        OauthProvider prov = OauthProvider.valueOf(provider.toUpperCase());
        Oauth2UserInfo info = oauth2Service.processOAuth2Login(prov, code);
        if(userService.isExitsts(info.getEmail()))
            userService.createUserFromOauth2(info);
        ResponseCookie accessToken = jwtInit.generaJwtCookie(info.getEmail());
        ResponseCookie refreshToken = jwtInit.generaJwtRefreshCookie(info.getEmail());

        return new TokenResponse(
                accessToken.toString(),
                refreshToken.toString()
        );
    }


}
