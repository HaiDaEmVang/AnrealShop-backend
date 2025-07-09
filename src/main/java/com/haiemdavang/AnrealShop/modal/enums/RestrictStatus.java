package com.haiemdavang.AnrealShop.modal.enums;

import lombok.Getter;

@Getter
public enum RestrictStatus {
    ALL("ALL", "Tất cả"),
    ACTIVE("ACTIVE", "Hoạt động"),
    VIOLATION("VIOLATION", "Vi phạm"),
    PENDING("PENDING", "Đang chờ duyệt"),
    HIDDEN("HIDDEN", "Bị ẩn");

    private final String id;
    private final String name;

    RestrictStatus(String id, String name) {
        this.id = id;
        this.name = name;
    }

}