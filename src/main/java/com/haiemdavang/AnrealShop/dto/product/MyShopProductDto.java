package com.haiemdavang.AnrealShop.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haiemdavang.AnrealShop.dto.shop.BaseShopDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
 @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyShopProductDto {
    private String id;
    private String name;
    private String thumbnailUrl;
    private String urlSlug;
    private String categoryId;
    private String categoryPath;
    private Long discountPrice;
    private Integer quantity;
    private Integer sold;
    private String status;
    private Boolean visible;
    private String createdAt;

    private String restrictedReason;
    private boolean isRestricted;

    private List<MyShopProductSkuDto> productSkus;
    private BaseShopDto baseShopDto;

}