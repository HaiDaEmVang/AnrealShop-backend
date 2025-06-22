package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.product.ProductDto;
import com.haiemdavang.AnrealShop.dto.product.ProductRequest;
import jakarta.validation.Valid;

public interface IProductService {
    ProductDto createProduct(@Valid ProductRequest productRequest);
}
