package com.haiemdavang.AnrealShop.modal.entity.address;

import com.haiemdavang.AnrealShop.modal.entity.shop.Shop; // Import entity Shop
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"shop", "province", "district", "ward"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "shop_addresses")
public class ShopAddress {

    @Id
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "sender_name", nullable = false, length = 100)
    private String senderName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String detail;
}