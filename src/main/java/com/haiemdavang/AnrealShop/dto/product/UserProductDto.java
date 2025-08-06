package com.haiemdavang.AnrealShop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProductDto {
    private String id;
    private String name;
    private String thumbnailUrl;
    private String sortDescription;
    private String urlSlug;
    private Long discountPrice;
    private Long price;
    private Integer quantity;
    private Integer sold;
    private float averageRating;
    private Integer totalReviews;

    private String categoryId;
    private String categoryName;

    private String shopId;
    private String shopName;
    private String shopThumbnailUrl;
}