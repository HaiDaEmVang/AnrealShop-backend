package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.config.ApplicationConfigInit;
import com.haiemdavang.AnrealShop.dto.user.RegisterRequest;
import com.haiemdavang.AnrealShop.dto.user.UserDto;
import com.haiemdavang.AnrealShop.dto.user.ProfileRequest;
import com.haiemdavang.AnrealShop.dto.auth.LoginRequest;
import com.haiemdavang.AnrealShop.dto.auth.Oauth2.OAuth2UserInfo;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
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
        user.setAvatarUrl(ApplicationConfigInit.IMAGE_USER_DEFAULT);
        return user;
    }
    
    public ProfileRequest toProfileRequest(User user) {
        if (user == null) {
            return null;
        }
        
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setFullName(user.getFullName());
        profileRequest.setPhoneNumber(user.getPhoneNumber());
        profileRequest.setGender(user.getGender());
        profileRequest.setDob(user.getDob());
        profileRequest.setAvatarUrl(user.getAvatarUrl());
        
        return profileRequest;
    }
    
    public User updateUserFromProfileRequest(User user, ProfileRequest profileRequest) {
        if (user == null || profileRequest == null) {
            return user;
        }
        
        user.setFullName(profileRequest.getFullName());
        user.setPhoneNumber(profileRequest.getPhoneNumber());
        user.setGender(profileRequest.getGender());
        user.setDob(profileRequest.getDob());
        
        if (profileRequest.getAvatarUrl() != null && !profileRequest.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(profileRequest.getAvatarUrl());
        }
        
        return user;
    }
    
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getPhoneNumber(),
            user.getAvatarUrl(),
            user.getGender(),
            user.getDob(),
            user.getRole() != null ? user.getRole().getName().toString() : null,
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
    public User toUser(LoginRequest loginRequest) {
        if (loginRequest == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPassword(loginRequest.getPassword());
        
        return user;
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
