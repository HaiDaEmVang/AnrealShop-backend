package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.cart.CartDto;
import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;

import java.util.Set;

public interface ICartService {
    int countByUserId(String userId);
    boolean addToCart(CartItemDto cartItemDto);
    void removeFromCart(String cartItemId);
    void clearCart(Set<String> cartItemIds);
    Set<CartDto> getCartItems();
    void updateQuantity(CartItemDto cartItemDto);
}