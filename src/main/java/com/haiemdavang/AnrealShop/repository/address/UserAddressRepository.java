package com.haiemdavang.AnrealShop.repository.address;

import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, String> {

    @EntityGraph(attributePaths = {"province", "district", "ward"})
    Optional<UserAddress> findByUserIdAndPrimaryAddressTrue(String userId);
    @EntityGraph(attributePaths = {"province", "district", "ward"})
    List<UserAddress> findAllByUserIdOrderByPrimaryAddressDesc(String userId);
    @EntityGraph(attributePaths = {"province", "district", "ward"})
    Optional<UserAddress> findByIdAndUserId(String id, String userId);

    List<UserAddress> findAllByUserIdAndIdNot(String userId, String id);
}
