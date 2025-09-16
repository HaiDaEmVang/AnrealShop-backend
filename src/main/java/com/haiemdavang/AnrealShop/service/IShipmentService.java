package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.shipping.search.SearchTypeShipping;
import com.haiemdavang.AnrealShop.dto.shipping.CartShippingFee;
import com.haiemdavang.AnrealShop.dto.shipping.CreateShipmentRequest;
import com.haiemdavang.AnrealShop.dto.shipping.MyShopShippingListResponse;
import com.haiemdavang.AnrealShop.dto.shipping.search.PreparingStatus;
import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;

import java.util.List;
import java.util.Map;

public interface IShipmentService {
    List<CartShippingFee> getShippingFeeForCart(List<String> cartItemIds);

    Map<ShopAddress, Long> getShippingFee(UserAddress userAddress, Map<ProductSku, Integer> productSkus);

    void createShipment(CreateShipmentRequest createShipmentRequest);

    MyShopShippingListResponse getListForShop(int page, int limit, String search, SearchTypeShipping searchTypeShipping, PreparingStatus preparingStatus, String sortBy);
}
