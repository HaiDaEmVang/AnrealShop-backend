package com.haiemdavang.AnrealShop.modal.entity.product;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductAttributeId {

    @Column(name = "product_id", length = 36)
    private String productId;

    @Column(name = "attribute_value_id", length = 36)
    private String attributeValueId;
}