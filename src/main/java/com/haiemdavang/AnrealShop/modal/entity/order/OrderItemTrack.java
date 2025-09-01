package com.haiemdavang.AnrealShop.modal.entity.order;

import com.haiemdavang.AnrealShop.modal.enums.OrderTrackStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "orderItem")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "order_item_tracks")
public class OrderItemTrack {

    @EmbeddedId
    private OrderItemTrackId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderItemId")
    @JoinColumn(name = "order_item_id", insertable = false, updatable = false)
    private OrderItem orderItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OrderTrackStatus status = OrderTrackStatus.PROCESSING;

    public LocalDateTime getUpdatedAt() {
        return (this.id != null) ? this.id.getUpdatedAt() : null;
    }

    public OrderItemTrack(OrderItem orderItem, OrderTrackStatus status, LocalDateTime updatedAt) {
        this.id = new OrderItemTrackId(orderItem.getId(), updatedAt);
        this.orderItem = orderItem;
        this.status = status;
    }

    public OrderItemTrack(OrderItem orderItem) {
        this.id = new OrderItemTrackId(orderItem.getId(), LocalDateTime.now());
        this.orderItem = orderItem;
        this.status = OrderTrackStatus.PROCESSING;
    }


}