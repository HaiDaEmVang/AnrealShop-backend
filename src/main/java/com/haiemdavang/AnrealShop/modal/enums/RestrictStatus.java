package com.haiemdavang.AnrealShop.modal.enums;

import lombok.Getter;

@Getter
public enum RestrictStatus {
    ALL("ALL", "Tất cả", 0),
    ACTIVE("ACTIVE", "Hoạt động", 1),
    VIOLATION("VIOLATION", "Vi phạm", 3),
    PENDING("PENDING", "Đang chờ duyệt", 2),
    HIDDEN("HIDDEN", "Bị ẩn", 4);

    private final String id;
    private final String name;
    private final int order;

    RestrictStatus(String id, String name, int order) {
        this.id = id;
        this.name = name;
        this.order = order;
    }

}