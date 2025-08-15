package com.haiemdavang.AnrealShop.modal.entity.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ShopOrderTrackId implements Serializable {

    @Column(name = "shop_order_id", nullable = false, length = 36)
    private String shopOrderId;

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}