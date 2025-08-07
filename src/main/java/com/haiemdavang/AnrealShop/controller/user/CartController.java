package com.haiemdavang.AnrealShop.controller.user;

import com.haiemdavang.AnrealShop.dto.cart.CartItemDto;
import com.haiemdavang.AnrealShop.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/cart")
public class CartController {

    private final ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody @Valid CartItemDto cartItemDto) {
        boolean isNew = cartService.addToCart(cartItemDto);
        return ResponseEntity.ok(Map.of("message","Product is added to cart", "isNew", isNew ));
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<?> updateQuantity(@RequestBody CartItemDto cartItemDto) {
        cartService.updateQuantity(cartItemDto);
        return ResponseEntity.ok(Map.of("message","Quantity is update to cart"));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String cartItemId) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.ok(Map.of("message","Product is remove to cart" ));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestBody Set<String> cartItemIds) {
        cartService.clearCart(cartItemIds);
        return ResponseEntity.ok(Map.of("message","Product list is remove to cart" ));
    }

    @GetMapping
    public ResponseEntity<?> getCart() {
        return ResponseEntity.ok(cartService.getCartItems());
    }
}