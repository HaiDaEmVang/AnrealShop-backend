package com.haiemdavang.AnrealShop.service;


import com.haiemdavang.AnrealShop.dto.auth.Oauth2.Oauth2UserInfo;
import com.haiemdavang.AnrealShop.modal.entity.user.User;

public interface IUserService {
    boolean isExitsts(String email);

    void resetPassword(String email, String password);

    User findByEmail(String email);                      

    void createUserFromOauth2(Oauth2UserInfo info);
}
