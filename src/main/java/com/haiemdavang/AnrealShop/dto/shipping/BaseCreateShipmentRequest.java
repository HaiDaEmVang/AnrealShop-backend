package com.haiemdavang.AnrealShop.dto.shipping;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseCreateShipmentRequest {

    private String note;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "SHIPMENT_PICKUP_DATE_NOT_NULL")
    private LocalDate pickupDate;
}