package com.haiemdavang.AnrealShop.modal.entity.shop;

import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "shopOrder")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "shop_order_tracks")
public class ShopOrderTrack {

    @EmbeddedId
    private ShopOrderTrackId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("shopOrderId")
    @JoinColumn(name = "shop_order_id", insertable = false, updatable = false)
    private ShopOrder shopOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OrderTrackStatus status = OrderTrackStatus.PROCESSING;

    public LocalDateTime getUpdatedAt() {
        return (this.id != null) ? this.id.getUpdatedAt() : null;
    }

    public ShopOrderTrack(ShopOrder shopOrder, OrderTrackStatus status, LocalDateTime updatedAt) {
        this.id = new ShopOrderTrackId(shopOrder.getId(), updatedAt);
        this.shopOrder = shopOrder;
        this.status = status;
    }

    public ShopOrderTrack(ShopOrder shopOrder) {
        this.id = new ShopOrderTrackId(shopOrder.getId(), LocalDateTime.now());
        this.shopOrder = shopOrder;
        this.status = OrderTrackStatus.PROCESSING;
    }
}