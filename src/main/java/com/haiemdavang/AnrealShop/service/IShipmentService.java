package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;

import java.util.List;

public interface IShipmentService {
    List<CartShippingFee> getShippingFeeForCart(List<String> cartItemIds);
}
