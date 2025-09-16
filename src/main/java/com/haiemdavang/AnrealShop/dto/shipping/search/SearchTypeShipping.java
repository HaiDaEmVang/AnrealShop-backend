package com.haiemdavang.AnrealShop.dto.shipping.search;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SearchTypeShipping {
    ORDER_CODE,
    CUSTOMER_NAME,
    SHIPPING_CODE;

    @JsonCreator
    public static SearchTypeShipping from(String value) {
        return SearchTypeShipping.valueOf(value.trim().toUpperCase());
    }
}
