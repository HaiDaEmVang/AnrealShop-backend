package com.haiemdavang.AnrealShop.service.shipment;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;
import com.haiemdavang.AnrealShop.dto.shipping.InfoShipment;
import com.haiemdavang.AnrealShop.dto.shipping.InfoShippingOrder;
import com.haiemdavang.AnrealShop.mapper.AddressMapper;
import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.cart.CartItem;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.service.IAddressService;
import com.haiemdavang.AnrealShop.service.ICartService;
import com.haiemdavang.AnrealShop.service.IShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImp implements IShipmentService {
    private final IGHNService ighnService;
    private final ICartService cartService;
    private final IAddressService addressService;

    private final AddressMapper addressMapper;

    @Override
    public List<CartShippingFee> getShippingFeeForCart(List<String> cartItemIds) {
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
                    .shopId(s.getId())
                    .fee(infoOrder.getFee())
                    .leadTime(infoOrder.getLeadTime())
                    .isSuccess(infoOrder.isSuccess)
                    .serviceName(infoOrder.getServiceName())
                    .build());
        }

        return result;
    }

    @Override
    public Map<ShopAddress, Long> getShippingFee(UserAddress userAddress, Map<ProductSku, Integer> productSkus) {
        Map<Shop, List<ProductSku>> productSkusByShop = productSkus.keySet().stream()
                .collect(Collectors.groupingBy(pk -> pk.getProduct().getShop(),
                        Collectors.mapping(pk -> pk, Collectors.toList())));

        Map<ShopAddress, Long> mapResult = new HashMap<>();
        for (Shop shop : productSkusByShop.keySet()) {
            ShopAddress shopAddress = addressService.getShopAddressById(shop.getId());
            int totalWeight = productSkusByShop.get(shop).stream()
                    .mapToInt(pk -> pk.getProduct().getWeight().intValue() * productSkus.get(pk))
                    .sum();
            InfoShipment info = InfoShipment.builder()
                    .from(addressMapper.toAddressDto(shopAddress))
                    .to(addressMapper.toAddressDto(userAddress))
                    .weight(totalWeight)
                    .build();
            int fetchFee = ighnService.getShippingOrderInfo(info).getFee();
            mapResult.put(shopAddress, (long) fetchFee);
        }
        return mapResult;

    }

}
