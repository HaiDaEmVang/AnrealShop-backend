package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.cart.CartDto;
import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;

import java.util.List;
import java.util.Set;

public interface ICartService {
    int countByUserId(String userId);
    boolean addToCart(CartItemDto cartItemDto);
    void removeFromCart(String cartItemId);
    int clearCart(List<String> cartItemIds);
    Set<CartDto> getCartItems();
    void updateQuantity(String cartItemId, int quantity);
}