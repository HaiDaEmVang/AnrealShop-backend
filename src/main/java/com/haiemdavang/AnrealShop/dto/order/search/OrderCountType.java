package com.haiemdavang.AnrealShop.dto.order.search;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderCountType {
    ONE, MORE, ALL;

    @JsonCreator
    public static OrderCountType from(String value) {
        return OrderCountType.valueOf(value.trim().toUpperCase());
    }
}
