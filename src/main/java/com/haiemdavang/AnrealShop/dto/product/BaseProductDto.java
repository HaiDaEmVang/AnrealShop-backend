package com.haiemdavang.AnrealShop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.haiemdavang.AnrealShop.dto.category.BaseCategoryDto;
import com.haiemdavang.AnrealShop.dto.shop.BaseShopDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseProductDto {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("sortDescription")
    private String sortDescription;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("slug")
    private String slug;
    
    @JsonProperty("price")
    private Long price;
    
    @JsonProperty("discountPrice")
    private Long discountPrice;
    
    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;
    
    @JsonProperty("quantity")
    private int quantity;
    
    @JsonProperty("averageRating")
    private float averageRating;
    
    @JsonProperty("totalReviews")
    private int totalReviews;
    
    @JsonProperty("revenue")
    private long revenue;
    
    @JsonProperty("sold")
    private int sold;
    
    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("restrictStatus")
    private String restrictStatus;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("updatedAt")
    private String updatedAt;
    
    @JsonProperty("shop")
    private BaseShopDto shop;
    
    @JsonProperty("category")
    private BaseCategoryDto category;
}
