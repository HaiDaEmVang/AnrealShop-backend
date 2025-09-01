package com.haiemdavang.AnrealShop.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class GHNResponse {

    @JsonProperty("CODAmount")
    private BigDecimal codAmount;

    @JsonProperty("CODTransferDate")
    private String codTransferDate; // null or date string, giữ String cho an toàn

    @JsonProperty("ClientOrderCode")
    private String clientOrderCode;

    @JsonProperty("ConvertedWeight")
    private Integer convertedWeight;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Fee")
    private FeeDto fee;

    @JsonProperty("Height")
    private Integer height;

    @JsonProperty("IsPartialReturn")
    private Boolean isPartialReturn;

    @JsonProperty("Length")
    private Integer length;

    @JsonProperty("OrderCode")
    private String orderCode;

    @JsonProperty("PartialReturnCode")
    private String partialReturnCode;

    @JsonProperty("PaymentType")
    private Integer paymentType;

    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("ReasonCode")
    private String reasonCode;

    @JsonProperty("ShopID")
    private Long shopId;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Time")
    private OffsetDateTime time; // ISO 8601 -> map chuẩn với Jackson

    @JsonProperty("TotalFee")
    private BigDecimal totalFee;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Warehouse")
    private String warehouse;

    @JsonProperty("Weight")
    private Integer weight;

    @JsonProperty("Width")
    private Integer width;

    @Data
    public static class FeeDto {
        @JsonProperty("CODFailedFee")
        private BigDecimal codFailedFee;

        @JsonProperty("CODFee")
        private BigDecimal codFee;

        @JsonProperty("Coupon")
        private BigDecimal coupon;

        @JsonProperty("DeliverRemoteAreasFee")
        private BigDecimal deliverRemoteAreasFee;

        @JsonProperty("DocumentReturn")
        private BigDecimal documentReturn;

        @JsonProperty("DoubleCheck")
        private BigDecimal doubleCheck;

        @JsonProperty("Insurance")
        private BigDecimal insurance;

        @JsonProperty("MainService")
        private BigDecimal mainService;

        @JsonProperty("PickRemoteAreasFee")
        private BigDecimal pickRemoteAreasFee;

        @JsonProperty("R2S")
        private BigDecimal r2s;

        @JsonProperty("Return")
        private BigDecimal returnFee;

        @JsonProperty("StationDO")
        private BigDecimal stationDO;

        @JsonProperty("StationPU")
        private BigDecimal stationPU;

        @JsonProperty("Total")
        private BigDecimal total;
    }
}
