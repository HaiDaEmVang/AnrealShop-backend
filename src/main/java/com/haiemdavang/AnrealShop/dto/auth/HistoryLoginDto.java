package com.haiemdavang.AnrealShop.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLoginDto {
    private String id;
    private LocalDateTime loginAt;
    private LocalDateTime logoutAt;
    private String userAgent;
    private String location;
    private String device;
    private boolean isCurrentSession;
    private boolean isActive;
}
