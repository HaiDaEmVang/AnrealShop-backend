package com.haiemdavang.AnrealShop.repository.address;

import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ShopAddressRepository extends JpaRepository<ShopAddress, String> {

    @EntityGraph(attributePaths = {"province", "district", "ward"})
    Optional<ShopAddress> findByShopIdAndPrimaryAddressTrue(String userId);
    @EntityGraph(attributePaths = {"province", "district", "ward"})
    List<ShopAddress> findAllByShopIdOrderByPrimaryAddressDesc(String userId);
    @EntityGraph(attributePaths = {"province", "district", "ward"})
    Optional<ShopAddress> findByIdAndShopId(String id, String shopId);

    List<ShopAddress> findAllByShopIdAndIdNot(String id, String id1);

    @EntityGraph(attributePaths = {"province", "district", "ward"})
    Set<ShopAddress> findByShopIdInAndPrimaryAddressTrue(Collection<String> shopIds);
}
