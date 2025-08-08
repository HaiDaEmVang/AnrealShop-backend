package com.haiemdavang.AnrealShop.dto.shipping;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoShipment {
    public AddressDto from;
    public AddressDto to;
    public int weight;
}
