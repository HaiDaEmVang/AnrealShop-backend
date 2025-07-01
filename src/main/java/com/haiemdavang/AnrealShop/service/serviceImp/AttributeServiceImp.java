package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.attribute.AttributeDto;
import com.haiemdavang.AnrealShop.dto.attribute.AttributeResponse;
import com.haiemdavang.AnrealShop.dto.attribute.ProductAttribute;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.mapper.AttributeMapper;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import com.haiemdavang.AnrealShop.repository.AttributeKeyRepository;
import com.haiemdavang.AnrealShop.repository.AttributeValueRepository;
import com.haiemdavang.AnrealShop.repository.ShopRepository;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.IAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeServiceImp implements IAttributeService {

    private final SecurityUtils securityUtils;
    private final ShopRepository shopRepository;
    private final AttributeKeyRepository attributeKeyRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeMapper attributeMapper;

    
    @Override
    public AttributeResponse getAttributesForShop() {
//        Shop shop = securityUtils.getCurrentUserShop();

        Shop shop = shopRepository.findById("shop-0c6a-1e3a-aa7b-4f10920bd9f0")
                .orElseThrow(() -> new BadRequestException("SHOP_NOT_FOUND"));

        List<AttributeKey> attributeKeys = attributeKeyRepository.findAllByIsDefaultTrueOrShopsContains(shop);
        return createAttributeResponses(attributeKeys);
    }


    private AttributeResponse createAttributeResponses(List<AttributeKey> attributeKeys) {
        List<AttributeValue> allAttributeValues = attributeValueRepository.findByAttributeKeyIn(attributeKeys);

        Map<AttributeKey, List<AttributeValue>> attributeMap = allAttributeValues.stream()
                .collect(Collectors.groupingBy(AttributeValue::getAttributeKey));

        List<AttributeDto> attributeDtoList = new ArrayList<>();
        List<ProductAttribute> productAttributes = new ArrayList<>();

        attributeKeys.forEach(key -> {
            List<AttributeValue> values = attributeMap.getOrDefault(key, new ArrayList<>());
            if (key.isForSku()) {
                productAttributes.add(attributeMapper.toProductAttribute(key, values));
            } else {
                attributeDtoList.add(attributeMapper.toAttributeDto(key, values));
            }
        });
        return AttributeResponse.builder()
                .attribute(attributeDtoList)
                .attributeForSku(productAttributes)
                .build();
    }
}
