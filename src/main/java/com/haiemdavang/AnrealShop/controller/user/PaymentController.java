package com.haiemdavang.AnrealShop.controller.user;


import com.haiemdavang.AnrealShop.dto.payment.PaymentResponseDto;
import com.haiemdavang.AnrealShop.exception.AnrealShopException;
import com.haiemdavang.AnrealShop.service.IOrderService;
import com.haiemdavang.AnrealShop.service.payment.VNPayService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class PaymentController {

    private final VNPayService vNPayService;
    private final IOrderService orderService;

    @Value("${fe_base_url}")
    private String feBaseUrl;

    @RequestMapping(value = "/IPN", method = {RequestMethod.GET, RequestMethod.POST})
    public String paymentIpn(@RequestParam(required = false) Map<String, String> params) {
        boolean isValid = vNPayService.validatePaymentResponse(params);
        if (!isValid) return "RspCode=97";
        String responseCode = params.get("vnp_ResponseCode");
        String orderId = params.get("vnp_TxnRef");

        if (responseCode != null && responseCode.equals("00")) {
            orderService.handleSuccessfulPayment(orderId);
            return "RspCode=00";
        } else {
            orderService.handleFailedPayment(orderId, responseCode);
            return "RspCode=" + responseCode;
        }
    }

    @GetMapping("/api/public/vnpay/payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> responseParams, HttpServletResponse response) {
        try {
            String orderId = responseParams.get("vnp_TxnRef");
            response.sendRedirect(feBaseUrl + "/payment/result/" + orderId);
        } catch (Exception e) {
            log.error("Error processing payment callback: ", e);
            throw new AnrealShopException("PAYMENT_CALLBACK_ERROR");
        }
    }

    @GetMapping("/api/payment/result/{orderId}")
    public ResponseEntity<PaymentResponseDto> getPaymentResult(@PathVariable String orderId) {
        PaymentResponseDto paymentResponseDto = orderService.getPaymentResult(orderId);
        return ResponseEntity.ok(paymentResponseDto);
    }

    private String getVnpayErrorMessage(String responseCode) {
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("01", "Giao dịch đã tồn tại");
        errorMessages.put("02", "Merchant không hợp lệ (kiểm tra lại vnp_TmnCode)");
        errorMessages.put("03", "Dữ liệu gửi sang không đúng định dạng");
        errorMessages.put("04", "Khởi tạo GD không thành công do Website đang bị tạm khóa");
        errorMessages.put("05", "Giao dịch không thành công do: Quý khách nhập sai mật khẩu thanh toán quá số lần quy định");
        errorMessages.put("06", "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP)");
        errorMessages.put("07", "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường)");
        errorMessages.put("09", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking");
        errorMessages.put("10", "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần");
        errorMessages.put("11", "Giao dịch không thành công do: Đã hết hạn chờ thanh toán");
        errorMessages.put("12", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa");
        errorMessages.put("13", "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP)");
        errorMessages.put("24", "Giao dịch không thành công do: Khách hàng hủy giao dịch");
        errorMessages.put("51", "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch");
        errorMessages.put("65", "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày");
        errorMessages.put("75", "Ngân hàng thanh toán đang bảo trì");
        errorMessages.put("79", "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán nhiều lần");
        errorMessages.put("99", "Có lỗi xảy ra trong quá trình xử lý");

        return errorMessages.getOrDefault(responseCode, "Giao dịch không thành công (Mã lỗi: " + responseCode + ")");
    }
}
