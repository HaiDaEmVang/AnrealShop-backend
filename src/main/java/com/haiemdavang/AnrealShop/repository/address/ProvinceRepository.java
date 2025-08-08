package com.haiemdavang.AnrealShop.repository.address;

import com.haiemdavang.AnrealShop.modal.entity.address.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, String> {
    List<Province> findByNameContainingIgnoreCase(String keyword);
}
