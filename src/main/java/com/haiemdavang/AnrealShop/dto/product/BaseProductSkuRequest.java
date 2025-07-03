package com.haiemdavang.AnrealShop.dto.product;

import com.haiemdavang.AnrealShop.dto.attribute.ProductAttribute;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseProductSkuRequest {
    @NotBlank(message = "PRODUCT_SKU_NOTBLANK")
    @Size(min = 3, max = 50, message = "PRODUCT_SKU_SIZE")
    private String sku;

    @NotNull(message = "PRODUCT_SKU_PRICE_NOTNULL")
    @Min(value = 1000, message = "PRODUCT_SKU_PRICE_MIN")
    private Long price;

    @NotNull(message = "PRODUCT_SKU_QUANTITY_NOTNULL")
    @Min(value = 0, message = "PRODUCT_SKU_QUANTITY_MIN")
    private Integer quantity;

    private String imageUrl;

    private List<ProductAttribute> attributes;
}
