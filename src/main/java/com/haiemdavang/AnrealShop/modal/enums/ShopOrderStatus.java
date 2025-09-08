package com.haiemdavang.AnrealShop.modal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopOrderStatus {
    INIT_PROCESSING("Đang xử lý"),
    PENDING_CONFIRMATION("Chờ xác nhận"),
    PREPARING("Chờ lấy hàng"),
    SHIPPING("Đang giao"),
    DELIVERED("Đã giao"),
    CLOSED("Hủy/Hoàn/Trả");

    private final String displayName;

}