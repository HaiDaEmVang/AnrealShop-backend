package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.attribute.AttributeDto;
import com.haiemdavang.AnrealShop.dto.attribute.ProductAttribute;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsAttribute;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttributeMapper {


    private EsAttribute toEsAttribute(ProductAttribute productAttribute) {
        if (productAttribute == null) {
            return null;
        }

        return EsAttribute.builder()
                .keyName(productAttribute.getAttributeKeyName())
                .displayName(productAttribute.getAttributeKeyDisplay())
                .value(productAttribute.getValue())
                .build();
    }
    public List<EsAttribute> toEsAttributes(List<ProductAttribute> productAttributes) {
        if (productAttributes == null || productAttributes.isEmpty()) {
            return new ArrayList<>();
        }

        return productAttributes.stream()
                .map(this::toEsAttribute)
                .toList();
    }
    

    public ProductAttribute toProductAttribute(AttributeKey attributeKey, List<AttributeValue> attributeValues) {
        List<String> values = attributeValues.stream()
                .map(AttributeValue::getValue)
                .collect(Collectors.toList());
                
        return ProductAttribute.builder()
                .attributeKeyName(attributeKey.getKeyName())
                .attributeKeyDisplay(attributeKey.getDisplayName())
                .value(values)
                .build();
    }
    
    public AttributeDto toAttributeDto(AttributeKey attributeKey, List<AttributeValue> attributeValues) {
        List<String> values = attributeValues.stream()
                .map(AttributeValue::getValue)
                .collect(Collectors.toList());
                
        return AttributeDto.builder()
                .attributeKeyName(attributeKey.getKeyName())
                .attributeKeyDisplay(attributeKey.getDisplayName())
                .value(values)
                .displayOrder(attributeKey.getDisplayOrder())
                .isDefault(attributeKey.isDefault())
                .isMultiSelect(attributeKey.isMultiSelected())
                .build();
    }

}
