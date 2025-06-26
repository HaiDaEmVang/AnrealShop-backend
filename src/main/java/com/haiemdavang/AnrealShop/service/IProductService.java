package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.product.BaseProductRequest;
import jakarta.validation.Valid;

public interface IProductService {
    void createProduct(@Valid BaseProductRequest baseProductRequest);
}
