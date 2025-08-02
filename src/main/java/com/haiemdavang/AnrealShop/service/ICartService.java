package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import java.util.List;

public interface ICartService {
    int countByUserId(String userId);
    void addToCart(CartItemDto cartItemDto);
    void removeFromCart(String productId);
    void clearCart(List<String> productIds);
    List<CartItemDto> getCartItems();
}