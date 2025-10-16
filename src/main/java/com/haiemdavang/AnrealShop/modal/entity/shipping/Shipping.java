package com.haiemdavang.AnrealShop.modal.entity.shipping;


import com.haiemdavang.AnrealShop.modal.entity.address.ShopAddress;
import com.haiemdavang.AnrealShop.modal.entity.address.UserAddress;
import com.haiemdavang.AnrealShop.modal.entity.shop.ShopOrder;
import com.haiemdavang.AnrealShop.modal.enums.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"addressFrom", "addressTo", "trackingHistory"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "shippings")
public class Shipping {

    @Id
    @Column(length = 36, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_from_id", nullable = false)
    private ShopAddress addressFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_to_id", nullable = false)
    private UserAddress addressTo;

    @Builder.Default
    @Column(name = "shipper_name", length = 100, nullable = false)
    private String shipperName = "";

    @Builder.Default
    @Column(name = "shipper_phone", length = 20, nullable = false)
    private String shipperPhone  = "";

    @Builder.Default
    @Column(name = "total_weight", nullable = false)
    private Long totalWeight = 0L;

    @Column(name = "fee", nullable = false)
    private Long fee;

    @Builder.Default
    @Column(name = "note")
    private String note  = "";

    @Builder.Default
    @Column(name = "is_printed", nullable = false)
    private boolean isPrinted  = false;

    @Builder.Default
    @Column(name = "day_pickup", nullable = false)
    private LocalDate dayPickup  = LocalDate.now().plusDays(1);

    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShippingStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_order_id", referencedColumnName = "id", nullable = false, unique = true)
    private ShopOrder shopOrder;

    @OneToMany(mappedBy = "shipping", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ShippingTrack> trackingHistory = new HashSet<>();

    public void addTrackingHistory(ShippingTrack track) {
        if (trackingHistory == null) trackingHistory = new HashSet<>();
        trackingHistory.add(track);
        track.setShipping(this);
    }

    public void setStatus(ShippingStatus status) {
        this.status = status;
        ShippingTrack track = new ShippingTrack(this, status, LocalDateTime.now());
        addTrackingHistory(track);
    }

}
