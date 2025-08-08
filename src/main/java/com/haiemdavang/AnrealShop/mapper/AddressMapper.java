package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.dto.address.AddressRequestDto;
import com.haiemdavang.AnrealShop.dto.address.BaseAddressDto;
import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import org.springframework.stereotype.Service;

@Service
public class AddressMapper {
    public AddressDto toAddressDto(UserAddress userAddressById) {
        if (userAddressById == null) {
            return null;
        }

        return AddressDto.builder()
                .id(userAddressById.getId())
                .receiverOrSenderName(userAddressById.getReceiverName())
                .phoneNumber(userAddressById.getPhoneNumber())
                .detailAddress(userAddressById.getDetail())
                .ProvinceId(userAddressById.getProvince().getId())
                .DistrictId(userAddressById.getDistrict().getId())
                .WardId(userAddressById.getWard().getId())
                .ProvinceName(userAddressById.getProvince().getName())
                .DistrictName(userAddressById.getDistrict().getName())
                .WardName(userAddressById.getWard().getName())
                .isPrimary(userAddressById.isPrimaryAddress())
                .build();
    }

    public AddressDto toAddressDto(ShopAddress shopAddress) {
        if (shopAddress == null) {
            return null;
        }

        return AddressDto.builder()
                .id(shopAddress.getId())
                .receiverOrSenderName(shopAddress.getSenderName())
                .phoneNumber(shopAddress.getPhoneNumber())
                .detailAddress(shopAddress.getDetail())
                .ProvinceId(shopAddress.getProvince().getId())
                .DistrictId(shopAddress.getDistrict().getId())
                .WardId(shopAddress.getWard().getId())
                .ProvinceName(shopAddress.getProvince().getName())
                .DistrictName(shopAddress.getDistrict().getName())
                .WardName(shopAddress.getWard().getName())
                .isPrimary(shopAddress.isPrimaryAddress())
                .build();
    }

    public UserAddress toUserAddress(AddressRequestDto addressRequestDto) {
        if (addressRequestDto == null) {
            return null;
        }

        return UserAddress.builder()
                .id(addressRequestDto.getId())
                .receiverName(addressRequestDto.getReceiverOrSenderName())
                .phoneNumber(addressRequestDto.getPhoneNumber())
                .detail(addressRequestDto.getDetailAddress())
                .primaryAddress(addressRequestDto.isPrimary())
                .build();
    }

    public ShopAddress toShopAddress(AddressRequestDto addressRequestDto) {
        if (addressRequestDto == null) {
            return null;
        }

        return ShopAddress.builder()
                .id(addressRequestDto.getId())
                .senderName(addressRequestDto.getReceiverOrSenderName())
                .phoneNumber(addressRequestDto.getPhoneNumber())
                .detail(addressRequestDto.getDetailAddress())
                .primaryAddress(addressRequestDto.isPrimary())
                .build();
    }

    public void updateUserAddress(UserAddress userAddress, AddressRequestDto addressRequestDto) {
        if (userAddress == null || addressRequestDto == null) {
            return;
        }

        userAddress.setReceiverName(addressRequestDto.getReceiverOrSenderName());
        userAddress.setPhoneNumber(addressRequestDto.getPhoneNumber());
        userAddress.setDetail(addressRequestDto.getDetailAddress());
        userAddress.setPrimaryAddress(addressRequestDto.isPrimary());

    }

    public void updateShopAddress(ShopAddress shopAddress, AddressRequestDto addressRequestDto) {
        if (shopAddress == null || addressRequestDto == null) {
            return;
        }

        shopAddress.setSenderName(addressRequestDto.getReceiverOrSenderName());
        shopAddress.setPhoneNumber(addressRequestDto.getPhoneNumber());
        shopAddress.setDetail(addressRequestDto.getDetailAddress());
        shopAddress.setPrimaryAddress(addressRequestDto.isPrimary());

    }

    public BaseAddressDto toBaseAddressDto(ShopAddress shopAddress) {
        if (shopAddress == null) {
            return null;
        }

        return BaseAddressDto.builder()
                .idProvince(Integer.parseInt(shopAddress.getProvince().getId()))
                .idDistrict(Integer.parseInt(shopAddress.getDistrict().getId()))
                .idWard(shopAddress.getWard().getId())
                .build();
    }

}
