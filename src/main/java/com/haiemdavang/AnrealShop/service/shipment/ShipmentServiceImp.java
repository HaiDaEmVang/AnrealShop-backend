package com.haiemdavang.AnrealShop.service.shipment;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;
import com.haiemdavang.AnrealShop.dto.shipping.CreateShipmentRequest;
import com.haiemdavang.AnrealShop.dto.shipping.InfoShipment;
import com.haiemdavang.AnrealShop.dto.shipping.InfoShippingOrder;
import com.haiemdavang.AnrealShop.exception.AnrealShopException;
import com.haiemdavang.AnrealShop.mapper.AddressMapper;
import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.cart.CartItem;
import com.haiemdavang.AnrealShop.modal.entity.order.Order;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItem;
import com.haiemdavang.AnrealShop.modal.entity.order.OrderItemTrack;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shipping.Shipping;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import com.haiemdavang.AnrealShop.modal.enums.ShippingStatus;
import com.haiemdavang.AnrealShop.repository.shipment.ShipmentRepository;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.IAddressService;
import com.haiemdavang.AnrealShop.service.ICartService;
import com.haiemdavang.AnrealShop.service.IShipmentService;
import com.haiemdavang.AnrealShop.service.order.IOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImp implements IShipmentService {
    private final IGHNService ighnService;
    private final ICartService cartService;
    private final IAddressService addressService;

    private final AddressMapper addressMapper;
    private final IOrderItemService orderItemService;
    private final ShipmentRepository shipmentRepository;
    private final SecurityUtils securityUtils;

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
            ShopAddress shopAddress = addressService.getShopAddressByIdShop(shop.getId());
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

    @Override
    @Transactional
    public void createShipment(CreateShipmentRequest createShipmentRequest) {
        List<OrderItem> orderItems = orderItemService.getForShipment(createShipmentRequest.getShopOrderIds());
//        ShopAddress fromAddress = addressService.getShopAddressByIdShop(createShipmentRequest.getAddressId());
        Shop currentShop = securityUtils.getCurrentUserShop();
        ShopAddress fromAddress = addressService.getShopAddressByIdShop(currentShop.getId());

        Map<Order, List<OrderItem>> orderListMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrder));

        for (Order shopOrder : orderListMap.keySet()) {
            Set<OrderItem> items = new HashSet<>(orderListMap.get(shopOrder));
            long totalWeight = items.stream()
                    .mapToInt(item -> item.getProductSku().getProduct().getWeight().intValue() * item.getQuantity())
                    .sum();
            InfoShipment info = InfoShipment.builder()
                    .from(addressMapper.toAddressDto(fromAddress))
                    .to(addressMapper.toAddressDto(shopOrder.getShippingAddress()))
                    .weight(totalWeight)
                    .build();
            InfoShippingOrder infoOrder = ighnService.getShippingOrderInfo(info);
            if (infoOrder.isSuccess) {
//                ighnService.createShippingOrder(shopOrder, items, infoOrder, createShipmentRequest.getNote());
                Shipping shipping = Shipping.builder()
                        .addressFrom(fromAddress)
                        .addressTo(shopOrder.getShippingAddress())
                        .totalWeight(totalWeight)
                        .fee((long) infoOrder.getFee())
                        .build();

                shipping.setStatus(ShippingStatus.ORDER_CREATED);
                shipping.setOrderItems(items);
                shipmentRepository.save(shipping);

                orderItemService.confirmOrderItems(items, OrderTrackStatus.WAIT_SHIPMENT);
            } else {
                throw new AnrealShopException("SERVER_ERROR");
            }
        }
    }

}
