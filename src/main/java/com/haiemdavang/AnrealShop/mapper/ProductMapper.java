package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.attribute.ProductAttribute;
import com.haiemdavang.AnrealShop.dto.product.*;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsProduct;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductMedia;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.enums.MediaType;
import com.haiemdavang.AnrealShop.utils.ApplicationInitHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMapper {
    private final ShopMapper shopMapper;
    private final CategoryMapper categoryMapper;
    private final AttributeMapper attributeMapper;

    public ProductMediaDto toProductMediaDto(ProductMedia productMedia) {
        if (productMedia == null) {
            return null;
        }
        return ProductMediaDto.builder()
                .id(productMedia.getId())
                .url(productMedia.getUrl())
                .thumbnailUrl(productMedia.getThumbnailUrl())
                .type(productMedia.getType().name())
                .build();
    }
    public ProductMedia toProductMedia(ProductMediaDto productMediaDto) {
        if (productMediaDto == null) {
            return null;
        }

        return ProductMedia.builder()
                .id(productMediaDto.getId())
                .url(productMediaDto.getUrl())
                .thumbnailUrl(productMediaDto.getThumbnailUrl())
                .type(MediaType.valueOf(productMediaDto.getType()))
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
                .height(baseProductRequest.getHeight())
                .length(baseProductRequest.getLength())
                .width(baseProductRequest.getWidth())
                .shop(shop)
                .build();

        List<ProductMedia> mediaList = new ArrayList<>();

        if (baseProductRequest.getMedia() != null && !baseProductRequest.getMedia().isEmpty()) {
            List<ProductMedia> productMediaList = baseProductRequest.getMedia().stream()
                    .map(mediaDto -> {
                        ProductMedia media = toProductMedia(mediaDto);
                        media.setProduct(product);
                        return media;
                    })
                    .toList();

            mediaList.addAll(productMediaList);

            baseProductRequest.getMedia().stream()
                    .filter(media -> "IMAGE".equals(media.getType()))
                    .findFirst()
                    .ifPresent(media -> product.setThumbnailUrl(media.getUrl()));
        }

        product.setMediaList(mediaList);

        return product;
    }
    public ProductSku toSkuEntity(BaseProductSkuRequest skuRequest, Product product) {
        if (skuRequest == null) {
            return null;
        }

        return ProductSku.builder()
                .sku(skuRequest.getSku())
                .price(skuRequest.getPrice())
                .quantity(skuRequest.getQuantity())
                .thumbnailUrl(skuRequest.getImageUrl())
                .product(product)
                .attributes(new HashSet<>())
                .build();
    }
    public EsProductDto toEsProductDto(Product product, List<ProductAttribute> attributeValues) {
        if (product == null) {
            return null;
        }


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
                .restrictStatus(product.getRestrictStatus() != null ? product.getRestrictStatus().getId() : null)
                .createdAt(product.getCreatedAt() == null ? LocalDateTime.now().toString() : product.getCreatedAt().toString())
                .updatedAt(product.getUpdatedAt() == null ? LocalDateTime.now().toString() : product.getUpdatedAt().toString())
                .shop(shopMapper.toBaseShopDto(product.getShop()))
                .category(categoryMapper.toBaseCategoryDto(product.getCategory()))
                .attributes(attributeValues)
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
                .restrictStatus(esProductDto.getRestrictStatus())
                .createdAt(LocalDateTime.parse(esProductDto.getCreatedAt()).atZone(ZoneOffset.systemDefault()).toInstant())
                .updatedAt(LocalDateTime.parse(esProductDto.getUpdatedAt()).atZone(ZoneOffset.systemDefault()).toInstant())
                .sold(esProductDto.getSold())
                .revenue(esProductDto.getRevenue())
                .averageRating(esProductDto.getAverageRating())
                .totalReviews(esProductDto.getTotalReviews())
                .visible(esProductDto.isVisible())
                .shop(shopMapper.toEsShop(esProductDto.getShop()))
                .categoryId(esProductDto.getCategory() != null ? esProductDto.getCategory().getId() : null)
                .attributes(attributeMapper.toEsAttributes(esProductDto.getAttributes()))
                .build();
    }
// myshop product mânager
    public MyShopProductSkuDto toMyShopProductSkuDto(ProductSku productSku) {
        if (productSku == null) {
            return null;
        }
        return MyShopProductSkuDto.builder()
                .id(productSku.getId())
                .sku(productSku.getSku())
                .imageUrl(productSku.getThumbnailUrl())
                .price(productSku.getPrice())
                .quantity(productSku.getQuantity())
                .sold(productSku.getSold())
                .createdAt(productSku.getCreatedAt() != null ? productSku.getCreatedAt().toString() : null)
                .build();
    }
    public MyShopProductDto toMyShopProductDto(Product product, List<ProductSku> productSkus) {
        if (product == null) {
            return null;
        }

        List<MyShopProductSkuDto> skuDtos = new ArrayList<>();
        if (productSkus != null && !productSkus.isEmpty()) {
            skuDtos = productSkus.stream()
                    .map(this::toMyShopProductSkuDto)
                    .toList();
        }
        return MyShopProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .thumbnailUrl(product.getThumbnailUrl())
                .urlSlug(product.getUrlSlug())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .discountPrice(product.getDiscountPrice())
                .quantity(product.getQuantity())
                .sold(product.getSold())
                .status(product.getRestrictStatus() != null ? product.getRestrictStatus().getId() : null)
                .visible(product.isVisible())
                .createdAt(product.getCreatedAt() != null ? product.getCreatedAt().toString() : null)
                .productSkus(skuDtos)
                .build();
    }


}
