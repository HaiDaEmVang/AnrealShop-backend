package com.haiemdavang.AnrealShop.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipService {
    private int service_id;
    private String short_name;
    private int service_type_id;
}
