package com.haiemdavang.AnrealShop.dto.sku;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeRequest {

    private String id;
    private String attributeKeyName;
    private String value;
}
