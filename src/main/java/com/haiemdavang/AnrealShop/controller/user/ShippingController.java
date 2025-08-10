package com.haiemdavang.AnrealShop.controller.user;

import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;
import com.haiemdavang.AnrealShop.service.IShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shipping")
public class ShippingController {
    private final IShipmentService shipmentService;

    @PostMapping("fee-for-cart")
    public ResponseEntity<List<CartShippingFee>> getFeeForCart(@RequestBody List<String> cartItemIds) {
        List<CartShippingFee> cartShippingFee = shipmentService.getShippingFeeForCart(cartItemIds);
        return ResponseEntity.ok(cartShippingFee);
    }

}
