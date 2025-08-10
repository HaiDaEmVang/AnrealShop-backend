package com.haiemdavang.AnrealShop.service.shipment;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;
import com.haiemdavang.AnrealShop.dto.shipping.InfoShipment;
import com.haiemdavang.AnrealShop.dto.shipping.InfoShippingOrder;
import com.haiemdavang.AnrealShop.exception.AnrealShopException;
import com.haiemdavang.AnrealShop.modal.entity.cart.CartItem;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.IAddressService;
import com.haiemdavang.AnrealShop.service.ICartService;
import com.haiemdavang.AnrealShop.service.IShipmentService;
import com.haiemdavang.AnrealShop.service.serviceImp.CartServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImp implements IShipmentService {
    private final IGHNService ighnService;
    private final ICartService cartService;
    private final IAddressService addressService;
    private final SecurityUtils securityUtils;

    @Override
    public List<CartShippingFee> getShippingFeeForCart(List<String> cartItemIds) {
        User user = securityUtils.getCurrentUser();
        Map<Shop, Set<CartItem>> cartItemsByShop = cartService.getCartItemsByIdIn(cartItemIds);
        Set<String> ids = cartItemsByShop.keySet().stream().map(Shop::getId).collect(Collectors.toSet());
        Map<String, AddressDto> shopAddresses = addressService.getShopAddressByIdIn(ids);
        AddressDto userAddress = addressService.findAddressPrimary();

        List<CartShippingFee> result = new ArrayList<>();

        for (Shop s: cartItemsByShop.keySet()) {
            AddressDto shopAddress = shopAddresses.get(s.getId());
            int totalWeight = cartItemsByShop.get(s).stream()
                    .mapToInt(item -> item.getProductSku().getProduct().getWeight().intValue() * item.getQuantity())
                    .sum();
            InfoShipment info = InfoShipment.builder()
                    .from(shopAddress)
                    .to(userAddress)
                    .weight(totalWeight)
                    .build();
            InfoShippingOrder infoOrder = ighnService.getShippingOrderInfo(info);
            result.add(CartShippingFee.builder()
                    .shopId(s.getId()).fee(infoOrder.getFee()).leadTime(infoOrder.getLeadTime()).build());
        }

        return result;
    }
}
