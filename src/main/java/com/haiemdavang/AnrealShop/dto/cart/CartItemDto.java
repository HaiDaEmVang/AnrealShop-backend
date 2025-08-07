package com.haiemdavang.AnrealShop.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haiemdavang.AnrealShop.dto.attribute.ProductAttributeSingleValueDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {
    private String id;
    private String productSkuId;
    private String thumbnailUrl; // tu product sku
    private int quantity; // tu cart item
    private Long price; // tu cart item
    private String name; // tu product sku -> product -> name
    private String attributeString; // tu product sku -> attributes -> value
//    product, cartItems, productSku, attributes
}
