package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.product.BaseProductRequest;
import com.haiemdavang.AnrealShop.dto.product.BaseProductSkuRequest;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductMedia;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import com.haiemdavang.AnrealShop.modal.enums.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductMapper {

    private final AttributeMapper attributeMapper;

    public Product toEntity(BaseProductRequest baseProductRequest, Category category, Shop shop) {
        Product product = Product.builder()
                .name(baseProductRequest.getName())
                .description(baseProductRequest.getDescription())
                .sortDescription(baseProductRequest.getSortDescription())
                .price(baseProductRequest.getPrice())
                .quantity(baseProductRequest.getQuantity())
                .category(category)
                .weight(baseProductRequest.getWeight())
                .shop(shop)
                .build();

        List<ProductMedia> mediaList = new ArrayList<>();

        if (baseProductRequest.getImageUrls() != null && !baseProductRequest.getImageUrls().isEmpty()) {
            List<ProductMedia> imageMediaList = baseProductRequest.getImageUrls().stream()
                    .map(url -> ProductMedia.builder()
                            .url(url)
                            .type(MediaType.IMAGE)
                            .product(product)
                            .build())
                    .toList();
                    
            mediaList.addAll(imageMediaList);

            product.setThumbnailUrl(baseProductRequest.getImageUrls().get(0));
        }

        if (baseProductRequest.getVideoUrl() != null && !baseProductRequest.getVideoUrl().isBlank()) {
            mediaList.add(ProductMedia.builder()
                    .url(baseProductRequest.getVideoUrl())
                    .type(MediaType.VIDEO)
                    .product(product)
                    .build());
        }

        product.setMediaList(mediaList);

        return product;
    }

    public ProductSku toSkuEntity(BaseProductSkuRequest skuRequest, Product product, Set<AttributeValue> attributeValues) {
        ProductSku productSku = ProductSku.builder()
                .sku(skuRequest.getSku())
                .price(skuRequest.getPrice())
                .quantity(skuRequest.getQuantity())
                .product(product)
                .attributes(attributeValues)
                .build();

        if (skuRequest.getImageUrl() != null && !skuRequest.getImageUrl().isBlank()) {
            productSku.setThumbnailUrl(skuRequest.getImageUrl());
        }
        
        return productSku;
    }

}
