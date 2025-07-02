//package com.haiemdavang.AnrealShop.controller.myshop;
//
//import com.haiemdavang.AnrealShop.dto.product.ProductDto;
//import com.haiemdavang.AnrealShop.dto.product.ProductRequest;
//import com.haiemdavang.AnrealShop.service.IProductService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//    private final IProductService productService;
//
//    @PostMapping("/create")
//    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductRequest productRequest) {
//        ProductDto productDto = productService.createProduct(productRequest);
//        return ResponseEntity.ok(productDto);
//    }
//
//}
