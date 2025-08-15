package com.haiemdavang.AnrealShop.repository.order;

import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, String> {
}
