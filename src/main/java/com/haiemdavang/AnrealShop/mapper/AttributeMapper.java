package com.haiemdavang.AnrealShop.mapper;

import co.elastic.clients.util.ObjectBuilder;
import com.haiemdavang.AnrealShop.dto.sku.AttributeRequest;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsAttribute;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import com.haiemdavang.AnrealShop.utils.ApplicationInitHelper;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Set;

@Service
public class AttributeMapper {

    public AttributeValue createAttributeValueFromRequest(String value, AttributeKey attributeKey) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setValue(value);
        attributeValue.setAttributeKey(attributeKey);
        return attributeValue;
    }

    public AttributeKey createAttributeKeyFromRequest(String keyName, Shop shop) {
        AttributeKey attributeKey = new AttributeKey();
        attributeKey.setDisplayName(keyName);
        attributeKey.setSlugName(ApplicationInitHelper.toSlug(keyName+ " " + shop.getName()));
        attributeKey.setShops(Set.of(shop));
        return attributeKey;
    }

    public AttributeRequest toAttributeRequest(AttributeValue attributeValue) {

        return AttributeRequest.builder()
                .id(attributeValue.getId())
                .attributeKeyName(attributeValue.getAttributeKey().getDisplayName())
                .value(attributeValue.getValue())
                .build();
    }

    public List<EsAttribute> toEsAttributes(List<AttributeRequest> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return null;
        }
        return attributes.stream()
                .map(this::toEsAttribute)
                .toList();
    }

    public EsAttribute toEsAttribute(AttributeRequest attributeRequest) {
        if (attributeRequest == null) {
            return null;
        }

        return EsAttribute.builder()
                .keyName(attributeRequest.getId())
                .displayName(attributeRequest.getAttributeKeyName())
                .value(attributeRequest.getValue())
                .build();
    }
}
