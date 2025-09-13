package com.haiemdavang.AnrealShop.modal.entity.shipping;

import com.haiemdavang.AnrealShop.modal.enums.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "shipping")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "shipping_tracks")
public class ShippingTrack {

    @EmbeddedId
    private ShippingTrackId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("shippingId")
    @JoinColumn(name = "shipping_id", insertable = false, updatable = false)
    private Shipping shipping;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ShippingStatus status = ShippingStatus.ORDER_CREATED;

    public LocalDateTime getUpdatedAt() {
        return (this.id != null) ? this.id.getUpdatedAt() : null;
    }

    public ShippingTrack(Shipping shipping, ShippingStatus status, LocalDateTime updatedAt) {
        this.id = new ShippingTrackId(shipping.getId(), updatedAt);
        this.shipping = shipping;
        this.status = status;
    }

    public ShippingTrack(Shipping shipping) {
        this.id = new ShippingTrackId(shipping.getId(), LocalDateTime.now());
        this.shipping = shipping;
        this.status = ShippingStatus.ORDER_CREATED;
    }


}