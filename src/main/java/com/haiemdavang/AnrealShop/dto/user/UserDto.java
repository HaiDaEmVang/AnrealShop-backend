package com.haiemdavang.AnrealShop.dto.user;

import com.haiemdavang.AnrealShop.modal.enums.GenderType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDto(
    String id,
    String username,
    String email,
    String fullName,
    String phoneNumber,
    String avatarUrl,
    GenderType gender,
    LocalDate dob,
    String role,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
