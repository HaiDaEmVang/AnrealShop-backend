package com.haiemdavang.AnrealShop.repository.notice;

import com.haiemdavang.AnrealShop.modal.entity.notification.ShopNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopNotificationRepository extends JpaRepository<ShopNotification, String> {
}
