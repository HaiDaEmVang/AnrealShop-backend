package com.haiemdavang.AnrealShop.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressRequestDto {
    private String id;
    private String receiverOrSenderName;
    private String phoneNumber;
    private String detailAddress;
    private String WardId;
    private boolean isPrimary;
}
