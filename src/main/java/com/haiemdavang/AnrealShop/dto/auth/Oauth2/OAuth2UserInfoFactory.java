package com.haiemdavang.AnrealShop.dto.auth.Oauth2;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static Oauth2UserInfo getOAuth2UserInfo(OauthProvider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> new GoogleOauth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("Provider " + provider + " is not supported yet.");
        };
    }
}
