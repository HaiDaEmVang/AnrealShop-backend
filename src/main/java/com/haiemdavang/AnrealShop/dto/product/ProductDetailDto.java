package com.haiemdavang.AnrealShop.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haiemdavang.AnrealShop.dto.attribute.ProductAttributeDto;
import com.haiemdavang.AnrealShop.dto.shop.BaseShopDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailDto {
    private String id;
    private String name;
    private String thumbnailUrl;
    private String urlSlug;
    private String categoryId; // nullable = true
    private String categoryPath; // nullable = true
    private String description; // nullable = true
    private String sortDescription; // nullable = true
    private Long price; // nullable = true
    private Long discountPrice;
    private Integer quantity;
    private Integer sold;
    private String status;
    private Boolean visible;

    private String createdAt;
    private String updatedAt; // nullable = true

    private String restrictedReason; // nullable = true
    private boolean isRestricted; // nullable = true
    private String restrictStatus; // nullable = true

    private float averageRating; // nullable = true
    private int totalReviews; // nullable = true

    private BigDecimal weight; // nullable = true
    private BigDecimal height; // nullable = true
    private BigDecimal length; // nullable = true
    private BigDecimal width; // nullable = true

    private BaseShopDto baseShopDto; // nullable = true
    private List<ProductMediaDto> medias; // nullable = true
    private List<ProductAttributeDto> attributes; // nullable = true
    private List<MyShopProductSkuDto> productSkus; // nullable = true
//    review nua ne .....

}