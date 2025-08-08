package com.haiemdavang.AnrealShop.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeeData {
    public int total;
    public int service_fee;
    public int insurance_fee;
    public int pick_station_fee;
    public int coupon_value;
    public int r2s_fee;
    public int document_return;
    public int double_check;
    public int cod_fee;
    public int pick_remote_areas_fee;
    public int deliver_remote_areas_fee;
    public int cod_failed_fee;
}
