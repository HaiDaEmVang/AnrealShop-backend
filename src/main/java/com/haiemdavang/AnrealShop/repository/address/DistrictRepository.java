package com.haiemdavang.AnrealShop.repository.address;

import com.haiemdavang.AnrealShop.modal.entity.address.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvinceId(String provinceId);
    List<District> findByProvinceIdAndNameContainingIgnoreCase(String provinceId, String keyword);
}
