package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.notice.SimpleNoticeMessage;
import com.haiemdavang.AnrealShop.modal.entity.notification.ShopNotification;
import com.haiemdavang.AnrealShop.modal.entity.notification.UserNotification;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public final class NoticeMapper {

    private static final String DEFAULT_THUMB_URL =
            "https://res.cloudinary.com/dlcjc36ow/image/upload/v1747916255/ImagError_jsv7hr.png";

    private NoticeMapper() {}

    public UserNotification toUserNotification(SimpleNoticeMessage msg, User user) {
        if (msg == null || user == null) {
            throw new IllegalArgumentException("msg and user must not be null");
        }
        return UserNotification.builder()
                .content(msg.getContent())
                .thumbnailUrl(resolveThumbnail(msg.getThumbnailUrl()))
                .user(user)
                .redirectUrl(msg.getRedirectUrl())
                .isRead(false)
                .build();
    }

    public ShopNotification toShopNotification(SimpleNoticeMessage msg, Shop shop) {
        if (msg == null || shop == null) {
            throw new IllegalArgumentException("msg and shop must not be null");
        }
        return ShopNotification.builder()
                .content(msg.getContent())
                .thumbnailUrl(resolveThumbnail(msg.getThumbnailUrl()))
                .shop(shop)
                .redirectUrl(msg.getRedirectUrl())
                .isRead(false)
                .build();
    }

    private String resolveThumbnail(String thumbnailUrl) {
        if (thumbnailUrl == null) return DEFAULT_THUMB_URL;
        String t = thumbnailUrl.trim();
        return t.isEmpty() ? DEFAULT_THUMB_URL : t;
    }
}