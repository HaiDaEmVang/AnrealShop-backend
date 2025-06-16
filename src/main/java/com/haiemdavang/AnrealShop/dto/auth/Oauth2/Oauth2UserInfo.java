package com.haiemdavang.AnrealShop.dto.auth.Oauth2;

import lombok.Data;

import java.util.Map;

@Data
public abstract class Oauth2UserInfo {
    protected Map<String, Object> attributes;

    public Oauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getUsername();

    public abstract String getEmail();

    public abstract String getFullName();

    public abstract String getAvatarUrl();
}
