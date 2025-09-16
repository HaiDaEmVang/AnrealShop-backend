package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.shipping.search.SearchTypeShipping;
import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;
import com.haiemdavang.AnrealShop.dto.shipping.CreateShipmentRequest;
import com.haiemdavang.AnrealShop.dto.shipping.MyShopShippingListResponse;
import com.haiemdavang.AnrealShop.dto.shipping.search.PreparingStatus;
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

    @GetMapping("/my-shop")
    public ResponseEntity<MyShopShippingListResponse> getListForShop(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "order_code") SearchTypeShipping searchTypeShipping,
            @RequestParam(required = false, defaultValue = "all") PreparingStatus preparingStatus,
            @RequestParam(required = false, defaultValue = "newest") String sortBy
    ) {
        MyShopShippingListResponse response = shipmentService.getListForShop(page, limit, search, searchTypeShipping, preparingStatus, sortBy);
        return ResponseEntity.ok(response);
    }

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
