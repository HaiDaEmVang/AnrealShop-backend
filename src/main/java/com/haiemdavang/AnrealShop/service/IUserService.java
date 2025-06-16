package com.haiemdavang.AnrealShop.service;


import com.haiemdavang.AnrealShop.dto.auth.Oauth2.Oauth2UserInfo;
import com.haiemdavang.AnrealShop.dto.user.ProfileRequest;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import com.haiemdavang.AnrealShop.dto.user.UserDto;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import jakarta.validation.Valid;

public interface IUserService {
    boolean isExitsts(String email);

    void resetPassword(String email, String password);

    User findByEmail(String email);                      

    void createUserFromOauth2(Oauth2UserInfo info);

    UserDto updateProfile(String email, @Valid ProfileRequest profileRequest);

    void deleteUser(String id);

    void registerUser(@Valid RegisterRequest request);
}
