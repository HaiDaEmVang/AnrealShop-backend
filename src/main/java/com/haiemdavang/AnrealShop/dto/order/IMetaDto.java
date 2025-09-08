package com.haiemdavang.AnrealShop.dto.order;

import com.haiemdavang.AnrealShop.modal.enums.ShopOrderStatus;

public interface IMetaDto {
    ShopOrderStatus getStatus();
    Long getTotalAmount();
    Integer getCount();


}
