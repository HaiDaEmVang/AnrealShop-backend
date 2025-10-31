package com.haiemdavang.AnrealShop.security.oauth2;

import com.haiemdavang.AnrealShop.security.jwt.JwtInit;
import com.haiemdavang.AnrealShop.service.auth.LoginHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtInit jwtInit;
    @Value("${server.fe.oauth2.redirect-uri:http://localhost:5173/login}")
    private String defaultTargetUrl;
    private final LoginHistoryService loginHistoryService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        ResponseCookie cookie_token = jwtInit.generaJwtCookie(authentication);
        ResponseCookie cookie_access_token = jwtInit.generaJwtRefreshCookie(authentication);

        response.addHeader("Set-Cookie", cookie_token.toString());
        response.addHeader("Set-Cookie", cookie_access_token.toString());

        String message = "Đăng nhập thành công!";
        String targetUrl = UriComponentsBuilder.fromUriString(defaultTargetUrl)
                .queryParam("success", URLEncoder.encode(message, StandardCharsets.UTF_8))
                .build()
                .toUriString();

        loginHistoryService.saveLoginHistory(request);

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
