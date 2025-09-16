package com.haiemdavang.AnrealShop.dto.order.search;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PreparingStatus {
    ALL,
    PREPARING,
    WAIT_SHIPMENT;

    @JsonCreator
    public static PreparingStatus from(String value) {
        return PreparingStatus.valueOf(value.trim().toUpperCase());
    }
}
