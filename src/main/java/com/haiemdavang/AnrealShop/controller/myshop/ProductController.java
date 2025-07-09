package com.haiemdavang.AnrealShop.controller.myshop;

import com.haiemdavang.AnrealShop.dto.product.BaseProductRequest;
import com.haiemdavang.AnrealShop.dto.product.MyShopProductListResponse;
import com.haiemdavang.AnrealShop.dto.product.ProductStatusDto;
import com.haiemdavang.AnrealShop.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/my-shop")
    public ResponseEntity<MyShopProductListResponse> getMyProduct(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false, defaultValue = "name-asc") String sortBy) {

        MyShopProductListResponse response = productService.getMyShopProducts(page, limit, status, search, categoryId, sortBy);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody BaseProductRequest baseProductRequest) {
        productService.createProduct(baseProductRequest);
        return ResponseEntity.ok(Map.of("message", "Product created successfully"));
    }

    @GetMapping("/suggest-my-products-by-name")
    public ResponseEntity<List<String>> suggestMyProductsName(@RequestParam String keyword) {
        List<String> productNames = productService.suggestMyProductsName(keyword);
        return ResponseEntity.ok(productNames);
    }

    @GetMapping("/filter-statuses")
    public ResponseEntity<List<ProductStatusDto>> getFilterMeta() {
        List<ProductStatusDto> response = productService.getFilterMeta();
        return ResponseEntity.ok(response);
    }

}
