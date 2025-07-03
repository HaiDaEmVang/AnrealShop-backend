package com.haiemdavang.AnrealShop.kafka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.haiemdavang.AnrealShop.dto.product.EsProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSyncMessage {
    private ProductSyncActionType action;
    private EsProductDto product;
}
