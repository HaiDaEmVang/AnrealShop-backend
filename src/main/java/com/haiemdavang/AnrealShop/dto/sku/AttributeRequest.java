package com.haiemdavang.AnrealShop.dto.sku;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeRequest {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("attributeKeyName")
    private String attributeKeyName;
    
    @JsonProperty("value")
    private String value;
}
