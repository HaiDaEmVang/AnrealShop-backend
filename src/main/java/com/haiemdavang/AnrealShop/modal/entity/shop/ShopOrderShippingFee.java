package com.haiemdavang.AnrealShop.modal.entity.shop;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "shopOrder")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "shop_order_shipping_fees")
public class ShopOrderShippingFee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false, name = "shop_order_id")
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "shop_order_id")
    private ShopOrder shopOrder;

    @Builder.Default
    private long amount = 0L;
}
