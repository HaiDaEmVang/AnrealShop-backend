package com.haiemdavang.AnrealShop.modal.entity.product;

import com.haiemdavang.AnrealShop.modal.enums.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "product_media")
public class ProductMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 255)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type = MediaType.IMAGE;
}
