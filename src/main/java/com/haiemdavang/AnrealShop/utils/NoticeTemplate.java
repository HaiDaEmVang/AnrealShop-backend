package com.haiemdavang.AnrealShop.utils;

import com.haiemdavang.AnrealShop.dto.notice.NoticeTemplateType;
import com.haiemdavang.AnrealShop.dto.notice.SimpleNoticeMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public final class NoticeTemplate {

    private NoticeTemplate() {}

    private static SimpleNoticeMessage base(String content, String thumbnailUrl, String receiveBy, String redirectUrl) {
        return SimpleNoticeMessage.builder()
                .id(UUID.randomUUID().toString())
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .receiveBy(receiveBy)
                .redirectUrl(redirectUrl)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static SimpleNoticeMessage orderPlaced(String orderId, BigDecimal total, String receiveBy) {
        String content = String.format("Your order %s has been placed. Total: %s", orderId, total);
        return base(content, null, receiveBy, "/orders/" + orderId);
    }

    public static SimpleNoticeMessage orderStatus(String orderId, String status, String receiveBy) {
        String content = String.format("Order %s status updated to %s.", orderId, status);
        return base(content, null, receiveBy, "/orders/" + orderId);
    }

    public static SimpleNoticeMessage promotion(String title, String promoId, String thumbnailUrl, String receiveBy) {
        String content = String.format("New promotion: %s", title);
        return base(content, thumbnailUrl, receiveBy, "/promotions/" + promoId);
    }

    public static SimpleNoticeMessage adminAlert(String message, String receiveBy) {
        return base("[Admin] " + message, null, receiveBy, "/admin/alerts");
    }

    public static SimpleNoticeMessage chat(String fromUser, String threadId, String preview, String receiveBy) {
        String content = String.format("New message from %s: %s", fromUser, preview);
        return base(content, null, receiveBy, "/chat/" + threadId);
    }

    public static SimpleNoticeMessage system(String message) {
        return base(message, null, "system", "/");
    }

    public static SimpleNoticeMessage toUser(String user, String message) {
        return base(message, null, user, "/");
    }




    // - ORDER_PLACED: orderId, total, receiveBy
    // - ORDER_STATUS_CHANGED: orderId, status, receiveBy
    // - PROMOTION: title, promoId, thumbnailUrl, receiveBy
    // - ADMIN_ALERT: message, receiveBy
    // - CHAT: fromUser, threadId, preview, receiveBy
    // - SYSTEM: message
    public static SimpleNoticeMessage fromType(NoticeTemplateType type, Map<String, String> args) {
        String receiveBy = args.getOrDefault("receiveBy", "system");
        return switch (type) {
            case ORDER_PLACED -> {
                BigDecimal total = new BigDecimal(args.getOrDefault("total", "0"));
                yield orderPlaced(args.getOrDefault("orderId", "N/A"), total, receiveBy);
            }
            case ORDER_STATUS_CHANGED -> orderStatus(
                    args.getOrDefault("orderId", "N/A"),
                    args.getOrDefault("status", "UNKNOWN"),
                    receiveBy
            );
            case PROMOTION -> promotion(
                    args.getOrDefault("title", "Promotion"),
                    args.getOrDefault("promoId", "0"),
                    args.get("thumbnailUrl"),
                    receiveBy
            );
            case ADMIN_ALERT -> adminAlert(args.getOrDefault("message", "Alert"), receiveBy);
            case CHAT -> chat(
                    args.getOrDefault("fromUser", "someone"),
                    args.getOrDefault("threadId", "0"),
                    args.getOrDefault("preview", "..."),
                    receiveBy
            );
            default -> system(args.getOrDefault("message", "Notification"));
        };
    }
}