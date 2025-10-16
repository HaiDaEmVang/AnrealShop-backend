package com.haiemdavang.AnrealShop.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAddressDto {
    private String id;
    private String receiverOrSenderName;
    private String phoneNumber;
    private String detailAddress;
}
