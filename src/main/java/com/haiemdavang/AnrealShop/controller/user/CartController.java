package com.haiemdavang.AnrealShop.controller.user;

import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import com.haiemdavang.AnrealShop.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/cart")
public class CartController {

    private final ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<? > addToCart(@RequestBody CartItemDto cartItemDto) {
        cartService.addToCart(cartItemDto);
        return ResponseEntity.ok(Map.of("message","Product is added to cart" ));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String productId) {
        cartService.removeFromCart(productId);
        return ResponseEntity.ok(Map.of("message","Product is remove to cart" ));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestBody List<String> productIds) {
        cartService.clearCart(productIds);
        return ResponseEntity.ok(Map.of("message","Product list is remove to cart" ));
    }

    @GetMapping
    public ResponseEntity<?> getCart() {
        return ResponseEntity.ok(cartService.getCartItems());
    }
}