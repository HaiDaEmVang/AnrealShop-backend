package com.haiemdavang.AnrealShop.repository.notice;

import com.haiemdavang.AnrealShop.modal.entity.notification.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, String> {
}
