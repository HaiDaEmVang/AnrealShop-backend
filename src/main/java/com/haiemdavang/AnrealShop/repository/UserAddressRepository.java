package com.haiemdavang.AnrealShop.repository;

import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, String> {
    UserAddress findByUserIdAndPrimaryAddressTrue(String userId);
    List<UserAddress> findAllByUserId(String userId);
}
