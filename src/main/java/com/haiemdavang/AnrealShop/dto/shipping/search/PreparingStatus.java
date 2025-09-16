package com.haiemdavang.AnrealShop.dto.shipping.search;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PreparingStatus {
    ALL,
    WAITING_FOR_PICKUP,
    PICK_UP;

    @JsonCreator
    public static PreparingStatus from(String value) {
        return PreparingStatus.valueOf(value.trim().toUpperCase());
    }
}
