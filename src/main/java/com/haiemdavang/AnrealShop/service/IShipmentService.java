package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;
import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;

import java.util.List;
import java.util.Map;

public interface IShipmentService {
    List<CartShippingFee> getShippingFeeForCart(List<String> cartItemIds);

    Map<ShopAddress, Integer> getShippingFee(UserAddress userAddress, Map<Product, Integer> productSkus);
}
