package com.haiemdavang.AnrealShop.modal.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum RestrictStatus {
    ALL("ALL", "Tất cả"),
    ACTIVE("ACTIVE", "Hoạt động" ),
    VIOLATION("VIOLATION", "Vi phạm" ),
    PENDING("PENDING", "Đang chờ duyệt" ),
    HIDDEN("HIDDEN", "Bị ẩn" );

    private final String id;
    private final String name;

    RestrictStatus(String id, String name ) {
        this.id = id;
        this.name = name;
    }

    public static List<RestrictStatus> getOrderDefault() {
        return Arrays.asList(ALL, ACTIVE, PENDING, VIOLATION, HIDDEN);
    }

    public static List<RestrictStatus> getOrderForAdmin() {
        return Arrays.asList(ALL, PENDING,  ACTIVE, VIOLATION);
    }

}