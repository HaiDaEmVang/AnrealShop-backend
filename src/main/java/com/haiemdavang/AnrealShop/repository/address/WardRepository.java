package com.haiemdavang.AnrealShop.repository.address;

import com.haiemdavang.AnrealShop.dto.address.IBaseAddressDto;
import com.haiemdavang.AnrealShop.modal.entity.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByDistrictId(String districtId);
    List<Ward> findByDistrictIdAndNameContainingIgnoreCase(String districtId, String keyword);

    @Query(value = "SELECT p as province, d as district, w as ward " +
            "FROM Ward w " +
            "JOIN District d ON w.district.id = d.id " +
            "JOIN Province p ON d.province.id = p.id " +
            "WHERE w.id = :wardId ")
    Optional<IBaseAddressDto> findProvinceAndDistrictAndWardByWardId(String wardId);
}
