package com.haiemdavang.AnrealShop.dto.order.search;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SearchType {
    ORDER_CODE,
    CUSTOMER_NAME,
    SHOP_NAME,
    PRODUCT_NAME;

    @JsonCreator
    public static SearchType from(String value) {
        return SearchType.valueOf(value.trim().toUpperCase());
    }
}
