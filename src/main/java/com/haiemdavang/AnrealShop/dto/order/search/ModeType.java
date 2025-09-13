package com.haiemdavang.AnrealShop.dto.order.search;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ModeType {
    HOME, SHIPPING;

    @JsonCreator
    public static ModeType from(String value) {
        return ModeType.valueOf(value.trim().toUpperCase());
    }
}
