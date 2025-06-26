package com.haiemdavang.AnrealShop.mapper;

import com.haiemdavang.AnrealShop.dto.sku.AttributeRequest;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
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
        attributeKey.setSlugName(toSlug(keyName, shop.getName()).substring(0, 50));
        attributeKey.setShops(Set.of(shop));
        return attributeKey;
    }

    private String toSlug(String str1, String str2) {
        String combined = str1 + " " + str2;
        String noDiacritics = Normalizer.normalize(combined, Normalizer.Form.NFD);
        noDiacritics = noDiacritics.replaceAll("\\p{M}", "");
        String slug = noDiacritics
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");

        return slug + "-" + System.currentTimeMillis();
    }



}
