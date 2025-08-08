package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.dto.address.AddressRequestDto;
import com.haiemdavang.AnrealShop.dto.address.BaseAddressDto;
import com.haiemdavang.AnrealShop.dto.address.SingleAddressDto;

import java.util.List;
import java.util.Set;

public interface IAddressService {
    AddressDto findAddressPrimary();
    List<AddressDto> findAll();

    AddressDto findShopAddressPrimary();
    List<AddressDto> findShopAll();

    Set<SingleAddressDto> getProvinceList(String keyword);

    Set<SingleAddressDto> getDistrictList(int provinceId, String keyword);

    Set<SingleAddressDto> getWardList(int districtId, String keyword);

    AddressDto createUserAddress(AddressRequestDto addressDto);

    AddressDto createShopAddress(AddressRequestDto addressDto);

    AddressDto updateUserAddress(String id, AddressRequestDto addressDto);

    AddressDto updateShopAddress(String id, AddressRequestDto addressDto);

    void deleteUserAddress(String id);

    void deleteShopAddress(String id);

    BaseAddressDto getShopAddress(String id);
}
