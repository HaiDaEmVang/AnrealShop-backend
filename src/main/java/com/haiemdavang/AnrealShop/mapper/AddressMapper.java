package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import org.springframework.stereotype.Service;

@Service
public class AddressMapper {
    public AddressDto toBaseAddressDto(UserAddress userAddress) {
        if (userAddress == null) {
            return null;
        }

        return AddressDto.builder()
                .id(userAddress.getId())
                .detailAddress(userAddress.getDetail())
                .ProvinceId(userAddress.getProvince() != null ? userAddress.getProvince().getId() : null)
                .DistrictId(userAddress.getDistrict() != null ? userAddress.getDistrict().getId() : null)
                .WardId(userAddress.getWard() != null ? userAddress.getWard().getId() : null)
                .ProvinceName(userAddress.getProvince() != null ? userAddress.getProvince().getName() : null)
                .DistrictName(userAddress.getDistrict() != null ? userAddress.getDistrict().getName() : null)
                .WardName(userAddress.getWard() != null ? userAddress.getWard().getName() : null)
                .build();
    }

    public AddressDto toAddressDto(UserAddress userAddress) {
        if (userAddress == null) {
            return null;
        }
        AddressDto baseAddressDto = toBaseAddressDto(userAddress);
        baseAddressDto.setUserId(userAddress.getUser() != null ? userAddress.getUser().getId() : null);
        baseAddressDto.setUserName(userAddress.getUser() != null ? userAddress.getUser().getUsername() : null);
        baseAddressDto.setReceiverName(userAddress.getReceiverName());
        baseAddressDto.setPhoneNumber(userAddress.getPhoneNumber());
        baseAddressDto.setIsDefault(String.valueOf(userAddress.isPrimaryAddress()));

        return baseAddressDto;
    }
}
