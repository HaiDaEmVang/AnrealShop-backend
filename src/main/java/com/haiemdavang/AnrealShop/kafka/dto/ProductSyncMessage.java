package com.haiemdavang.AnrealShop.kafka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.haiemdavang.AnrealShop.dto.product.EsProductDto;
import com.haiemdavang.AnrealShop.modal.enums.RestrictStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSyncMessage {
    private ProductSyncActionType action;
    private String id;
    private Set<String> ids;
    private EsProductDto product;
    private boolean isVisible;
    private RestrictStatus status;
}
