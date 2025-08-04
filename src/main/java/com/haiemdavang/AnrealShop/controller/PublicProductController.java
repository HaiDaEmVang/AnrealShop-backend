package com.haiemdavang.AnrealShop.controller;

import com.haiemdavang.AnrealShop.dto.product.*;
import com.haiemdavang.AnrealShop.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/products")
public class PublicProductController {
    private final IProductService productService;


    @GetMapping("/")
    public ResponseEntity<List<UserProductDto>> getProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false, defaultValue = "newest") String sortBy) {

        List<UserProductDto> response = productService.getProducts(page, limit, search, categoryId, sortBy);
        return ResponseEntity.ok(response);
    }

}
