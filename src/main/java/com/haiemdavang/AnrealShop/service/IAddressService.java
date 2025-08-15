package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.dto.address.AddressRequestDto;
import com.haiemdavang.AnrealShop.dto.address.BaseAddressDto;
import com.haiemdavang.AnrealShop.dto.address.SingleAddressDto;
import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAddressService {
    AddressDto findAddressPrimary();
    List<AddressDto> findAll();

    AddressDto findShopAddressPrimary();
    List<AddressDto> findShopAll();

    Set<SingleAddressDto> getProvinceList(String keyword);

    Set<SingleAddressDto> getDistrictList(String provinceId, String keyword);

    Set<SingleAddressDto> getWardList(String districtId, String keyword);

    AddressDto createUserAddress(AddressRequestDto addressDto);

    AddressDto createShopAddress(AddressRequestDto addressDto);

    AddressDto updateUserAddress(String id, AddressRequestDto addressDto);

    AddressDto updateShopAddress(String id, AddressRequestDto addressDto);

    void deleteUserAddress(String id);

    void deleteShopAddress(String id);

    Map<String, AddressDto> getShopAddressByIdIn(Set<String> shopIds);

    UserAddress getCurrentUserAddressById(@NotNull @NotBlank(message = "ADDRESS_ID_NOT_BLANK") String addressId);

    ShopAddress getShopAddressById(String id);
}
