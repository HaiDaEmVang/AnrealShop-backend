package com.haiemdavang.AnrealShop.dto.shipping;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateShipmentRequest {
    @NotEmpty(message = "{SHIPMENT_SHOP_ORDER_IDS_NOT_EMPTY}")
    private List<String> shopOrderIds;

    @NotBlank(message = "{SHIPMENT_ADDRESS_ID_NOT_BLANK}")
    private String addressId;

    private String note;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "SHIPMENT_PICKUP_DATE_NOT_NULL")
    private LocalDate pickupDate;
}