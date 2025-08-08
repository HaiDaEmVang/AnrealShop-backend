package com.haiemdavang.AnrealShop.dto.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseAddressDto {
    private int idProvince;
    private int idDistrict;
    private String idWard;
}
