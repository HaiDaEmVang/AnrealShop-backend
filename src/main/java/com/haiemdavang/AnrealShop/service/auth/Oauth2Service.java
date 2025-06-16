package com.haiemdavang.AnrealShop.service.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.haiemdavang.AnrealShop.dto.auth.Oauth2.OAuth2UserInfoFactory;
import com.haiemdavang.AnrealShop.dto.auth.Oauth2.Oauth2UserInfo;
import com.haiemdavang.AnrealShop.dto.auth.Oauth2.OauthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class Oauth2Service {

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    public Oauth2UserInfo processOAuth2Login(OauthProvider provider, String code) {
        String accessToken;
        Map<String, Object> userAttributes;

        switch (provider) {
            case GOOGLE:
                accessToken = getGoogleAccessToken(code);
                userAttributes = getGoogleUserInfo(accessToken);
                break;
            default:
                throw new IllegalArgumentException("Provider not supported: " + provider);
        }

        return OAuth2UserInfoFactory.getOAuth2UserInfo(provider, userAttributes);
    }

    private String getGoogleAccessToken(String code) {
        String tokenUri = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        JsonNode response = restTemplate.postForObject(tokenUri, params, JsonNode.class);
        assert response != null;
        return response.get("access_token").asText();
    }

    private Map<String, Object> getGoogleUserInfo(String accessToken) {
        String userInfoUri = "https://www.googleapis.com/oauth2/v2/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class).getBody();
    }

}