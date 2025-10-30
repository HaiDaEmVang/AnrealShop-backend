package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.auth.Oauth2.OAuth2UserInfo;
import com.haiemdavang.AnrealShop.dto.user.ProfileRequest;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import com.haiemdavang.AnrealShop.dto.user.UserDto;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.utils.ApplicationInitHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserMapper {

    public User createUserFromRegisterRequest(RegisterRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        String username = request.email().split("@")[0];
        user.setUsername(username);
        user.setPassword(request.password());
        user.setAvatarUrl(ApplicationInitHelper.IMAGE_USER_DEFAULT);
        return user;
    }
    
    public User updateUserFromProfileRequest(User user, ProfileRequest profileRequest) {
        if (user == null || profileRequest == null) {
            return user;
        }
        
        user.setFullName(profileRequest.getFullName());
        user.setPhoneNumber(profileRequest.getPhoneNumber());
        user.setGender(profileRequest.getGender());
        user.setDob(profileRequest.getDob());
        user.setAvatarUrl(profileRequest.getAvatarUrl());
        
        if (profileRequest.getAvatarUrl() != null && !profileRequest.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(profileRequest.getAvatarUrl());
        }
        
        return user;
    }
    
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .dob(user.getDob())
                .isVerified(user.isVerify())
                .role(user.getRole() != null ? user.getRole().getName().toString() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    
    public User createUserFromOauth2UserInfo(OAuth2UserInfo oauth2UserInfo) {
        if (oauth2UserInfo == null) {
            return null;
        }
        
        User user = new User();
        user.setEmail(oauth2UserInfo.getEmail());
        user.setUsername(oauth2UserInfo.getUsername());
        user.setFullName(oauth2UserInfo.getFullName());
        user.setAvatarUrl(oauth2UserInfo.getAvatarUrl());
        user.setPassword(UUID.randomUUID().toString());
        user.setFromSocial(true);
        
        return user;
    }


}
