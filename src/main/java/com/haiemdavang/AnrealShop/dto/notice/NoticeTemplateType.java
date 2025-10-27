package com.haiemdavang.AnrealShop.dto.notice;

import lombok.Getter;

@Getter
public enum NoticeTemplateType {
    ORDER_PLACED,
    ORDER_STATUS_CHANGED,
    PROMOTION,
    ADMIN_ALERT,
    CHAT,
    SYSTEM
}