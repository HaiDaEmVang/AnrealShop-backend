package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.checkout.ItemProductCheckoutDto;
import com.haiemdavang.AnrealShop.dto.checkout.CheckoutInfoDto;
import com.haiemdavang.AnrealShop.dto.checkout.CheckoutRequestDto;
import com.haiemdavang.AnrealShop.dto.checkout.CheckoutResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface ICheckoutService {

    CheckoutResponseDto checkout(@Valid CheckoutRequestDto requestDto, HttpServletRequest request);

    void validateItems(List<ItemProductCheckoutDto> items);

    List<CheckoutInfoDto> getListCheckout(Map<String, Integer> idProductSkus);
}
