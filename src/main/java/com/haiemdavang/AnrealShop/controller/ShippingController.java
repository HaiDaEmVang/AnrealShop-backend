package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;
import com.haiemdavang.AnrealShop.dto.shipping.CreateShipmentRequest;
import com.haiemdavang.AnrealShop.service.IShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shipping")
public class ShippingController {
    private final IShipmentService shipmentService;

    @PostMapping("/fee-for-cart")
    public ResponseEntity<List<CartShippingFee>> getFeeForCart(@RequestBody List<String> cartItemIds) {
        List<CartShippingFee> cartShippingFee = shipmentService.getShippingFeeForCart(cartItemIds);
        return ResponseEntity.ok(cartShippingFee);
    }

    @PutMapping("/create-shipments")
    public ResponseEntity<?> create(@RequestBody CreateShipmentRequest createShipmentRequest) {
        shipmentService.createShipment(createShipmentRequest);
        return ResponseEntity.ok(Map.of("message", "Create shipping order successfully"));
    }
}
