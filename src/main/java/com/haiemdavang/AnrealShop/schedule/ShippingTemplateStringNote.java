package com.haiemdavang.AnrealShop.schedule;

import java.util.List;

public class ShippingTemplateStringNote {
    public static final String ORDER_CREATED_NOTES = "Đơn hàng của bạn đã được tạo thành công và đang chờ xử lý.";
    public static final String PICKED_UP_NOTES = "Tài xế đã lấy hàng thành công.";
    public static final String DELIVERED_NOTES = "Đơn hàng đã được giao thành công.";

    public static final List<String> IN_TRANSIT_NOTES = List.of(
            "Đơn hàng đã đến kho trung chuyển SOC.",
            "Đơn hàng đang được luân chuyển giữa các bưu cục.",
            "Đơn hàng đã rời kho và đang trên đường đến kho phân loại khu vực.",
            "Đơn hàng đang ở trung tâm phân loại."
    );

    public static final List<String> OUT_FOR_DELIVERY_NOTES = List.of(
            "Bưu tá đang trên đường giao hàng đến bạn.",
            "Đơn hàng dự kiến sẽ được giao trong hôm nay. Vui lòng để ý điện thoại.",
            "Tài xế đang đi giao. Số điện thoại: [Số điện thoại tài xế]."
    );

    public static final List<String> DELIVERY_FAILED_NOTES = List.of(
            "Giao hàng không thành công. Lý do: Không liên lạc được với khách hàng.",
            "Giao hàng thất bại. Khách hẹn giao lại vào ngày mai.",
            "Giao hàng không thành công. Lý do: Sai địa chỉ.",
            "Không liên lạc được với người nhận sau 3 lần gọi.",
            "Khách từ chối nhận hàng."
    );

    public static final List<String> RETURNED_NOTES = List.of(
            "Đơn hàng đã được hoàn trả về kho người gửi thành công.",
            "Hoàn hàng thành công do giao thất bại nhiều lần.",
            "Đã hoàn hàng theo yêu cầu của người gửi.",
            "Người nhận từ chối nhận hàng. Đã hoàn trả về kho."
    );

}
