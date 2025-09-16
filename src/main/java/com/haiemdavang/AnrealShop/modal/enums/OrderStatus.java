package com.haiemdavang.AnrealShop.modal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PROCESSING("Đang xử lý"),
    SUCCESS("Thành công"),
    CANCELED("Thất bại");

    private final String displayName;

}