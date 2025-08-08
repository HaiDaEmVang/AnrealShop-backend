package com.haiemdavang.AnrealShop.dto.address;

import com.haiemdavang.AnrealShop.modal.entity.address.District;
import com.haiemdavang.AnrealShop.modal.entity.address.Province;
import com.haiemdavang.AnrealShop.modal.entity.address.Ward;

public interface IBaseAddressDto {
    Province getProvince();
    District getDistrict();
    Ward getWard();
}
