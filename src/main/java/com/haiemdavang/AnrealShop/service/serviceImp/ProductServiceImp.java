package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.product.BaseProductRequest;
import com.haiemdavang.AnrealShop.dto.product.BaseProductSkuRequest;
import com.haiemdavang.AnrealShop.dto.sku.AttributeRequest;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.mapper.AttributeMapper;
import com.haiemdavang.AnrealShop.mapper.ProductMapper;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import com.haiemdavang.AnrealShop.repository.*;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImp implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ShopRepository shopRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeKeyRepository attributeKeyRepository;
    private final SecurityUtils securityUtils;
    private final ProductMapper productMapper;
    private final AttributeMapper attributeMapper;

    @Override
    @Transactional
    public void createProduct(BaseProductRequest baseProductRequest) {
//        Shop currentUserShop = securityUtils.getCurrentUserShop();

        Shop currentUserShop = shopRepository.findById("shop-0c6a-1e3a-aa7b-4f10920bd9f0")
                .orElseThrow(() -> new BadRequestException("SHOP_NOT_FOUND"));

        Category category = categoryRepository.findById(baseProductRequest.getCategoryId())
                .orElseThrow(() -> new BadRequestException("CATEGORY_NOT_FOUND"));

        Product product = productMapper.toEntity(baseProductRequest, category, currentUserShop);

        Product savedProduct = productRepository.save(product);

        Set<String> allRequestedAttributeKeyNames = baseProductRequest.getProductSkus().stream()
                .flatMap(skuReq -> skuReq.getAttributes().stream())
                .map(AttributeRequest::getAttributeKeyName)
                .collect(Collectors.toSet());

        Map<String, AttributeKey> existingAttributeKeysMap = new HashMap<>();
        if (!allRequestedAttributeKeyNames.isEmpty()) {
            attributeKeyRepository.findByDisplayNameIn(allRequestedAttributeKeyNames.stream().toList())
                    .forEach(ak -> existingAttributeKeysMap.put(ak.getDisplayName(), ak));
        }

        List<AttributeKey> newAttributeKeys = new ArrayList<>();
        for (String keyName : allRequestedAttributeKeyNames) {
            if (!existingAttributeKeysMap.containsKey(keyName)) {
                AttributeKey newAk = attributeMapper.createAttributeKeyFromRequest(keyName, currentUserShop);
                newAttributeKeys.add(newAk);
            }else {
                AttributeKey keyCheck = existingAttributeKeysMap.get(keyName);
                if(!keyCheck.isDefault() && !keyCheck.getShops().contains(currentUserShop)){
                    keyCheck.getShops().add(currentUserShop);
                    newAttributeKeys.add(keyCheck);
                }
            }
        }
        if (!newAttributeKeys.isEmpty()) {
            List<AttributeKey> savedNewAttributeKeys = attributeKeyRepository.saveAll(newAttributeKeys);
            savedNewAttributeKeys.forEach(ak -> existingAttributeKeysMap.put(ak.getDisplayName(), ak));
        }

        Set<AbstractMap.SimpleEntry<String, String>> allRequestedKeyValuePairs = baseProductRequest.getProductSkus().stream()
                .flatMap(skuReq -> skuReq.getAttributes().stream())
                .map(attrDto -> new AbstractMap.SimpleEntry<>(attrDto.getAttributeKeyName(), attrDto.getValue()))
                .collect(Collectors.toSet());

        Map<AbstractMap.SimpleEntry<String, String>, AttributeValue> existingAttributeValues = new HashMap<>();

        if (!allRequestedKeyValuePairs.isEmpty()) {
            Map<String, List<String>> valuesByAttributeKeyName = allRequestedKeyValuePairs.stream()
                    .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())));

            for (Map.Entry<String, List<String>> entry : valuesByAttributeKeyName.entrySet()) {
                String keyName = entry.getKey();
                List<String> values = entry.getValue();
                AttributeKey attributeKey = existingAttributeKeysMap.get(keyName);
                if (attributeKey != null) {
                    attributeValueRepository.findByAttributeKeyAndValueIn(attributeKey, values)
                            .forEach(av -> existingAttributeValues.put(new AbstractMap.SimpleEntry<>(keyName, av.getValue()), av));
                }
            }
        }

        List<AttributeValue> newAttributeValues = new ArrayList<>();
        for (AbstractMap.SimpleEntry<String, String> keyValuePair : allRequestedKeyValuePairs) {
            if (!existingAttributeValues.containsKey(keyValuePair)) {
                String keyName = keyValuePair.getKey();
                String value = keyValuePair.getValue();

                AttributeKey attributeKey = existingAttributeKeysMap.get(keyName);
                if (attributeKey == null) {
                    log.warn("AttributeKey '{}' not found, but it should have been created or retrieved. Skipping AttributeValue.", keyName);
                    continue;
                }

                AttributeValue newAv = attributeMapper.createAttributeValueFromRequest(value, attributeKey);
                newAttributeValues.add(newAv);
            }
        }
        if (!newAttributeValues.isEmpty()) {
            List<AttributeValue> savedNewAttributeValues = attributeValueRepository.saveAll(newAttributeValues);
            savedNewAttributeValues.forEach(av -> existingAttributeValues.put(new AbstractMap.SimpleEntry<>(av.getAttributeKey().getDisplayName(), av.getValue()), av));
        }

        List<ProductSku> productSkusToSave = new ArrayList<>();
        for (BaseProductSkuRequest skuRequest : baseProductRequest.getProductSkus()) {
            Set<AttributeValue> associatedAttributeValues = new HashSet<>();
            for (AttributeRequest attrReq : skuRequest.getAttributes()) {
                String keyName = attrReq.getAttributeKeyName();
                String value = attrReq.getValue();
                AbstractMap.SimpleEntry<String, String> keyValuePair = new AbstractMap.SimpleEntry<>(keyName, value);

                AttributeValue attributeValue = existingAttributeValues.get(keyValuePair);
                if (attributeValue == null) {
                    log.warn("AttributeValue for key '{}' and value '{}' not found, but it should have been created or retrieved. Skipping association.", keyName, value);
                    continue;
                }
                associatedAttributeValues.add(attributeValue);
            }

            ProductSku productSku = productMapper.toSkuEntity(skuRequest, savedProduct, associatedAttributeValues);
            productSkusToSave.add(productSku);
        }

        if (!productSkusToSave.isEmpty()) {
            productSkuRepository.saveAll(productSkusToSave);
        }
    }
}
