package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.product.BaseProductRequest;
import com.haiemdavang.AnrealShop.dto.product.BaseProductSkuRequest;
import com.haiemdavang.AnrealShop.dto.product.EsProductDto;
import com.haiemdavang.AnrealShop.dto.sku.AttributeRequest;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsProduct;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductMedia;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import com.haiemdavang.AnrealShop.modal.enums.MediaType;
import com.haiemdavang.AnrealShop.utils.ApplicationInitHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductMapper {
    private final ShopMapper shopMapper;
    private final CategoryMapper categoryMapper;
    private final AttributeMapper attributeMapper;

    public EsProductDto toEsProductDto(Product product, List<AttributeValue> attributeValues) {
        if (product == null) {
            return null;
        }

        List<AttributeRequest> attributes = attributeValues.stream()
                .map(attributeMapper::toAttributeRequest)
                .toList();

        return EsProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sortDescription(product.getSortDescription())
                .slug(product.getUrlSlug())
                .thumbnailUrl(product.getThumbnailUrl())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .quantity(product.getQuantity())
                .averageRating(product.getAverageRating())
                .totalReviews(product.getTotalReviews())
                .revenue(product.getRevenue())
                .sold(product.getSold())
                .visible(product.isVisible())
                .createdAt(product.getCreatedAt() == null ? LocalDateTime.now().toString() : product.getCreatedAt().toString())
                .updatedAt(product.getUpdatedAt() == null ? LocalDateTime.now().toString() : product.getUpdatedAt().toString())
                .shop(shopMapper.toBaseShopDto(product.getShop()))
                .category(categoryMapper.toBaseCategoryDto(product.getCategory()))
                .attributes(attributes)
                .build();
    }

    public EsProduct toEsProduct(EsProductDto esProductDto) {
        if (esProductDto == null) {
            return null;
        }

        return EsProduct.builder()
                .id(esProductDto.getId())
                .name(esProductDto.getName())
                .sortDescription(esProductDto.getSortDescription())
                .description(esProductDto.getDescription())
                .suggest(esProductDto.getName())
                .urlSlug(esProductDto.getSlug())
                .price(esProductDto.getPrice())
                .discountPrice(esProductDto.getDiscountPrice())
                .quantity(esProductDto.getQuantity())
                .thumbnailUrl(esProductDto.getThumbnailUrl())
                .createdAt(LocalDateTime.parse(esProductDto.getCreatedAt()).atZone(ZoneOffset.systemDefault()).toInstant())
                .updatedAt(LocalDateTime.parse(esProductDto.getUpdatedAt()).atZone(ZoneOffset.systemDefault()).toInstant())
                .sold(esProductDto.getSold())
                .revenue(esProductDto.getRevenue())
                .averageRating(esProductDto.getAverageRating())
                .totalReviews(esProductDto.getTotalReviews())
                .visible(esProductDto.isVisible())
                .shop(shopMapper.toEsShop(esProductDto.getShop()))
                .category(categoryMapper.toEsCategory(esProductDto.getCategory()))
                .attributes(attributeMapper.toEsAttributes(esProductDto.getAttributes()))
                .build();
    }
    public Product toEntity(BaseProductRequest baseProductRequest, Category category, Shop shop) {
        Product product = Product.builder()
                .name(baseProductRequest.getName())
                .urlSlug(ApplicationInitHelper.toSlug(baseProductRequest.getName()))
                .description(baseProductRequest.getDescription())
                .sortDescription(baseProductRequest.getSortDescription())
                .price(baseProductRequest.getPrice())
                .discountPrice(baseProductRequest.getDiscountPrice())
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
