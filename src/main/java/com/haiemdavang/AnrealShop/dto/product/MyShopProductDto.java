package com.haiemdavang.AnrealShop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyShopProductDto {
    private String id;
    private String name;
    private String thumbnailUrl;
    private String urlSlug;
    private String categoryId;
    private Long discountPrice;
    private Integer quantity;
    private Integer sold;
    private String status;
    private Boolean visible;
    private String createdAt;
    private List<MyShopProductSkuDto> productSkus;
}