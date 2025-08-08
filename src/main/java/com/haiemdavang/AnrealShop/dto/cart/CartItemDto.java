package com.haiemdavang.AnrealShop.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haiemdavang.AnrealShop.dto.attribute.ProductAttributeSingleValueDto;
import com.haiemdavang.AnrealShop.dto.shipping.InfoShippingOrder;
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
    private String thumbnailUrl;
    @Min(value = 1, message = "QUANTITY_MUST_BE_GREATER_THAN_OR_EQUAL_TO_1")
    private int quantity;
    private int maxQuantity;
    private Long price;
    private String name;
    private String attributeString;

//    shipping
    private InfoShippingOrder infoShippingOrder;
}
