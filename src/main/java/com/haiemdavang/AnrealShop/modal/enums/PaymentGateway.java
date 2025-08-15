package com.haiemdavang.AnrealShop.modal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.haiemdavang.AnrealShop.exception.BadRequestException;

public enum PaymentGateway {
    VNPAY("vnpay"),
    CASH_ON_DELIVERY("cash_on_delivery");

    private final String value;

    PaymentGateway(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PaymentGateway fromString(String value) {
        for (PaymentGateway type : PaymentGateway.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BadRequestException("INVALID_PAYMENT_GATEWAY");
    }
}
