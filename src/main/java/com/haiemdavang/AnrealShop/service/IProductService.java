package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.product.BaseProductRequest;
import com.haiemdavang.AnrealShop.dto.product.MyShopProductDto;
import com.haiemdavang.AnrealShop.dto.product.MyShopProductListResponse;
import com.haiemdavang.AnrealShop.dto.product.ProductStatusDto;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import jakarta.validation.Valid;

import java.util.List;

public interface IProductService {
    void createProduct(@Valid BaseProductRequest baseProductRequest);
    List<String> suggestMyProductsName(String keyword);
    List<ProductStatusDto> getFilterMeta();

    MyShopProductListResponse getMyShopProducts(int page, int limit, String status, String search, String categoryId, String sortBy);

    MyShopProductDto updateProduct(String id, BaseProductRequest baseProductRequest);

    void delete(String id, boolean isForce);

    Product getProductById(String id);

    Product getProductByIdAndThrow(String id);

    MyShopProductDto updateProductVisible(String id, boolean visible);
}
