package com.haiemdavang.AnrealShop.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.haiemdavang.AnrealShop.dto.attribute.ProductAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EsProductDto extends BaseProductDto{
    @JsonProperty("attributes")
    private List<ProductAttribute> attributes;
}
