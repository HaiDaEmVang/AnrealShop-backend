package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.attribute.AttributeDto;
import com.haiemdavang.AnrealShop.dto.attribute.ProductAttribute;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsAttribute;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import com.haiemdavang.AnrealShop.utils.ApplicationInitHelper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttributeMapper {
 
    public AttributeValue createAttributeValueFromRequest(String value, AttributeKey attributeKey) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setValue(value);
        attributeValue.setAttributeKey(attributeKey);
        return attributeValue;
    }

    
    public AttributeValue toAttributeValue(String value, AttributeKey attributeKey) {
        return AttributeValue.builder()
                .value(value)
                .attributeKey(attributeKey)
                .build();
    }
    
    public AttributeValue toAttributeValue(String value, AttributeKey attributeKey, boolean isDefault, int displayOrder) {
        return AttributeValue.builder()
                .value(value)
                .attributeKey(attributeKey)
                .isDefault(isDefault)
                .displayOrder(displayOrder)
                .build();
    }
    
    public EsAttribute toEsAttribute(AttributeKey attributeKey, List<String> values) {
        return EsAttribute.builder()
                .keyName(attributeKey.getKeyName())
                .displayName(attributeKey.getDisplayName())
                .value(values)
                .build();
    }
    
    public List<EsAttribute> toEsAttributes(Map<AttributeKey, List<AttributeValue>> attributeMap) {
        if (attributeMap == null || attributeMap.isEmpty()) {
            return Collections.emptyList();
        }
        
        return attributeMap.entrySet().stream()
                .map(entry -> {
                    AttributeKey key = entry.getKey();
                    List<String> values = entry.getValue().stream()
                            .map(AttributeValue::getValue)
                            .collect(Collectors.toList());
                    return toEsAttribute(key, values);
                })
                .collect(Collectors.toList());
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
    
    public List<ProductAttribute> toProductAttributes(Map<AttributeKey, List<AttributeValue>> attributeMap) {
        if (attributeMap == null || attributeMap.isEmpty()) {
            return Collections.emptyList();
        }
        
        return attributeMap.entrySet().stream()
                .map(entry -> toProductAttribute(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
    
    public List<AttributeDto> toAttributeDtos(Map<AttributeKey, List<AttributeValue>> attributeMap) {
        if (attributeMap == null || attributeMap.isEmpty()) {
            return Collections.emptyList();
        }
        
        return attributeMap.entrySet().stream()
                .map(entry -> toAttributeDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(AttributeDto::getDisplayOrder))
                .collect(Collectors.toList());
    }
}
