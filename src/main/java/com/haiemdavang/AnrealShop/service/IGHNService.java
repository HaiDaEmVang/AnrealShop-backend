package com.haiemdavang.AnrealShop.service;

import com.haiemdavang.AnrealShop.dto.shipping.InfoShipment;
import com.haiemdavang.AnrealShop.dto.shipping.InfoShippingOrder;

public interface IGHNService {
    InfoShippingOrder getShippingOrderInfo(InfoShipment infoShipment);
}
