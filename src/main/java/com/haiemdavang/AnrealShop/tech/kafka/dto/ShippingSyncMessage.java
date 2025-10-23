package com.haiemdavang.AnrealShop.tech.kafka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.haiemdavang.AnrealShop.modal.enums.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingSyncMessage {
    private String id;
    private String note;
    private ShippingStatus status;

    public static ShippingSyncMessage from(String id, ShippingStatus status, String note) {
        return ShippingSyncMessage.builder()
                .id(id)
                .status(status)
                .note(note)
                .build();
    }
}
