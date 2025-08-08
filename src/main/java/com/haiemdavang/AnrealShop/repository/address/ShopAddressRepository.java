package com.haiemdavang.AnrealShop.repository.address;

import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopAddressRepository extends JpaRepository<ShopAddress, String> {

    @EntityGraph(attributePaths = {"province", "district", "ward"})
    Optional<ShopAddress> findByShopIdAndPrimaryAddressTrue(String userId);
    @EntityGraph(attributePaths = {"province", "district", "ward"})
    List<ShopAddress> findAllByShopId(String userId);
    @EntityGraph(attributePaths = {"province", "district", "ward"})
    Optional<ShopAddress> findByIdAndShopId(String id, String shopId);

    List<ShopAddress> findAllByShopIdAndIdNot(String id, String id1);
}
