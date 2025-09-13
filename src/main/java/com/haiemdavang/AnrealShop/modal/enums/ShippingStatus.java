package com.haiemdavang.AnrealShop.modal.enums;

public enum ShippingStatus {
    ORDER_CREATED,       // Đơn đã được tạo thành công (chờ xử lý tiếp)
    WAITING_FOR_PICKUP,  // Đơn đang chờ shipper đến lấy hàng từ cửa hàng/kho
    PICKED_UP,           // Shipper đã lấy hàng từ cửa hàng/kho
    IN_TRANSIT,          // Hàng đang được vận chuyển giữa các điểm trung chuyển/kho
    OUT_FOR_DELIVERY,    // Shipper đang trên đường giao cho khách
    DELIVERED,           // Hàng đã được giao thành công cho khách
    DELIVERY_FAILED,     // Giao hàng thất bại (khách không nhận, sai địa chỉ, hoặc sự cố khác)
    RETURNED             // Hàng đã được hoàn trả về cho cửa hàng/kho
}

