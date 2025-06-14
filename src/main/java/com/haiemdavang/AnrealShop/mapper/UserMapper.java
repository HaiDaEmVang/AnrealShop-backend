package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.user.ProfileRequest;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    
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
}
