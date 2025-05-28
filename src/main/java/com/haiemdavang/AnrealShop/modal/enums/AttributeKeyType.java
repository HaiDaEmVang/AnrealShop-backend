package com.haiemdavang.AnrealShop.modal.enums;

public enum AttributeKeyType {
    COLOR("Màu Sắc"),
    SIZE("Kích Thước"),
    MATERIAL("Chất Liệu"),
    STYLE("Phong cách"),
    PATTERN("Mẫu"),
    TARGET_AUDIENCE("Đối Tượng"),
    BRAND("Thương hiệu"),
    ORIGIN("Xuất xứ");


    private final String displayName;

    AttributeKeyType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}