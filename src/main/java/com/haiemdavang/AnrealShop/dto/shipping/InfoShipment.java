package com.haiemdavang.AnrealShop.dto.shipping;

import com.haiemdavang.AnrealShop.dto.address.BaseAddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoShipment {
    public BaseAddressDto from;
    public BaseAddressDto to;
    public int weight;
    public int height;
    public int width;
    public int length;
}
