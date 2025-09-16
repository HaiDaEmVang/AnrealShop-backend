package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.shipping.ShippingItem;
import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentMapper {


    public ShippingItem toShippingItems(Shipping shipping, ShopOrder shop) {
        return ShippingItem.builder()
                .shippingId(shipping.getId())
                .shippingStatus(shipping.getStatus().name())
                .dayPickup(shipping.getDayPickup().toString())
                .isPrinted(shipping.isPrinted())

                .shopOrderId(shop.getId())
                .countOrderItems(shipping.getOrderItems().size())
                .createdAt(shop.getCreatedAt().toString())

                .customerId(shop.getUser().getId())
                .customerName(shop.getUser().getFullName())
                .customerPhone(shop.getUser().getPhoneNumber())

                .confirmationTime(shipping.getCreatedAt().toString())
                .shippingMethod("GHN")

                .build();


    }
}
