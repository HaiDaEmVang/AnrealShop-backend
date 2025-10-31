package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.auth.HistoryLoginDto;
import com.haiemdavang.AnrealShop.modal.entity.user.HistoryLogin;
import org.springframework.stereotype.Service;

@Service
public class HistoryLoginMapper {

    public HistoryLoginDto toHistoryLoginDto(HistoryLogin historyLogin) {
        if (historyLogin == null) {
            return null;
        }

        return HistoryLoginDto.builder()
                .id(historyLogin.getId())
                .loginAt(historyLogin.getLoginAt())
                .logoutAt(historyLogin.getLogoutAt())
                .userAgent(historyLogin.getUserAgent())
                .location(historyLogin.getLocation())
                .device(extractOperatingSystem(historyLogin.getDevice()))
                .isActive(historyLogin.getLogoutAt() == null || historyLogin.getLogoutAt().isAfter(historyLogin.getLoginAt()))
                .isCurrentSession(false)
                .build();
    }

    private String extractOperatingSystem(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("windows")) return "Windows";
        if (userAgent.contains("mac")) return "macOS";
        if (userAgent.contains("linux")) return "Linux";
        if (userAgent.contains("android")) return "Android";
        if (userAgent.contains("iphone") || userAgent.contains("ipad")) return "iOS";
        return "Other";
    }
}