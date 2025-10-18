package com.haiemdavang.AnrealShop.dto.order.search;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PreparingStatus {
    ALL,
    PREPARING,
    CONFIRMED;

    @JsonCreator
    public static PreparingStatus from(String value) {
        return PreparingStatus.valueOf(value.trim().toUpperCase());
    }
}
