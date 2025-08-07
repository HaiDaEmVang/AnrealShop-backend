package com.haiemdavang.AnrealShop.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haiemdavang.AnrealShop.dto.attribute.ProductAttributeSingleValueDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {
    private String id;
    private String productId;
    private String urlSlug;
    private String productSkuId;
    private String thumbnailUrl; // tu product sku
    @Min(value = 1, message = "QUANTITY_MUST_BE_GREATER_THAN_OR_EQUAL_TO_1")
    private int quantity; // tu cart item
    private int maxQuantity; // tu product sku -> max quantity
    private Long price; // tu cart item
    private String name; // tu product sku -> product -> name
    private String attributeString; // tu product sku -> attributes -> value
//    product, cartItems, productSku, attributes
}
