package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;

import java.util.List;

public interface IAddressService {
    AddressDto findAddressPrimaryByUserId(String userId);
    List<AddressDto> findAllByUserId(String userId);
}
