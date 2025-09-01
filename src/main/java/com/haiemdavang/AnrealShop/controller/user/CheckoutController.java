package com.haiemdavang.AnrealShop.controller.user;

import com.haiemdavang.AnrealShop.dto.checkout.CheckoutInfoDto;
import com.haiemdavang.AnrealShop.dto.checkout.CheckoutRequestDto;
import com.haiemdavang.AnrealShop.dto.checkout.CheckoutResponseDto;
import com.haiemdavang.AnrealShop.service.ICheckoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final ICheckoutService checkoutService;

    @PostMapping("/items")
        public ResponseEntity<List<CheckoutInfoDto>> getListCheckout(@RequestBody Map<String, Integer> itemsCheckoutRequest) {
        return ResponseEntity.ok(checkoutService.getListCheckout(itemsCheckoutRequest));
    }

    @PostMapping
    public ResponseEntity<CheckoutResponseDto> checkout(@RequestBody @Valid CheckoutRequestDto requestDto, HttpServletRequest request) {
        checkoutService.validateItems(requestDto.getItems());
        return ResponseEntity.ok(checkoutService.checkout(requestDto, request));
    }

}
