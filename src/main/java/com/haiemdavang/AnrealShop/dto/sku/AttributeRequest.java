package com.haiemdavang.AnrealShop.dto.sku;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "ATTRIBUTE_KEY_NAME_NOTBLANK")
    @JsonProperty("attributeKeyName")
    private String attributeKeyName;

    @NotBlank(message = "ATTRIBUTE_VALUE_NOTBLANK")
    @JsonProperty("value")
    private String value;
}
