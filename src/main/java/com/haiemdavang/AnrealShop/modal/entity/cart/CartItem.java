package com.haiemdavang.AnrealShop.modal.entity.cart;

import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"cart", "product", "selectedAttributes"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long price;

    @Column(columnDefinition = "INT DEFAULT 1")
    private int quantity = 1;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean selected = true;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cart_item_attributes", // Tên bảng nối
            joinColumns = @JoinColumn(name = "cart_item_id"), // Khóa ngoại trong bảng nối trỏ về CartItem
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id") // THAY ĐỔI: Khóa ngoại trỏ về AttributeValue
    )
    private Set<AttributeValue> selectedAttributes;
}