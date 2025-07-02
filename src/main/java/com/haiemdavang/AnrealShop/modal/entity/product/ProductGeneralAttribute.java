package com.haiemdavang.AnrealShop.modal.entity.product;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_general_attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"productId", "attributeValueId"})
public class ProductGeneralAttribute {
    @EmbeddedId
    private ProductAttributeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("attributeValueId")
    @JoinColumn(name = "attribute_value_id", nullable = false)
    private AttributeValue attributeValue;
}
