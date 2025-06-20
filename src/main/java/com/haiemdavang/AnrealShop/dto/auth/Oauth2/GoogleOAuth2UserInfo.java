package com.haiemdavang.AnrealShop.dto.auth.Oauth2;

import com.haiemdavang.AnrealShop.config.ApplicationConfigInit;

import java.util.Map;
import java.util.Optional;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getUsername() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getFullName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getAvatarUrl() {
        return Optional.ofNullable(attributes.get("picture"))
                .map(Object::toString)
                .orElse(ApplicationConfigInit.IMAGE_USER_DEFAULT);
    }
}