package com.haiemdavang.AnrealShop.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    private String id;
    private String UserId;
    private String UserName;
    private String receiverName;
    private String phoneNumber;
    private String detailAddress;
    private String ProvinceId;
    private String DistrictId;
    private String WardId;
    private String ProvinceName;
    private String DistrictName;
    private String WardName;
    private String isDefault;
}
