package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.attribute.ProductAttribute;
import com.haiemdavang.AnrealShop.dto.product.*;
import com.haiemdavang.AnrealShop.elasticsearch.service.ProductIndexerService;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncActionType;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncMessage;
import com.haiemdavang.AnrealShop.kafka.producer.ProductKafkaProducer;
import com.haiemdavang.AnrealShop.mapper.ProductMapper;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.sku.AttributeValue;
import com.haiemdavang.AnrealShop.modal.enums.RestrictStatus;
import com.haiemdavang.AnrealShop.repository.AttributeKeyRepository;
import com.haiemdavang.AnrealShop.repository.AttributeValueRepository;
import com.haiemdavang.AnrealShop.repository.ProductSkuRepository;
import com.haiemdavang.AnrealShop.repository.ShopRepository;
import com.haiemdavang.AnrealShop.repository.product.ProductRepository;
import com.haiemdavang.AnrealShop.repository.product.ProductSpecification;
import com.haiemdavang.AnrealShop.security.SecurityUtils;
import com.haiemdavang.AnrealShop.service.ICategoryService;
import com.haiemdavang.AnrealShop.service.IProductService;
import com.haiemdavang.AnrealShop.utils.ApplicationInitHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImp implements IProductService {
    private final ProductRepository productRepository;
    private final ICategoryService categoryService;
    private final ProductSkuRepository productSkuRepository;
    private final ShopRepository shopRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeKeyRepository attributeKeyRepository;
    private final ProductMapper productMapper;

    private final ProductKafkaProducer productKafkaProducer;
    private final ProductIndexerService productIndexerService;

    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public void createProduct(BaseProductRequest baseProductRequest) {
//        Shop currentUserShop = securityUtils.getCurrentUserShop();

        Shop currentUserShop = shopRepository.findById("shop-0c6a-1e3a-aa7b-4f10920bd9f0")
                .orElseThrow(() -> new BadRequestException("SHOP_NOT_FOUND"));

        Category category = categoryService.findById(baseProductRequest.getCategoryId());

        Product product = productMapper.toEntity(baseProductRequest, category, currentUserShop);

        List<ProductAttribute> allAttributes = new ArrayList<>();


        if (baseProductRequest.getAttributes() != null && !baseProductRequest.getAttributes().isEmpty()) {
            Set<ProductAttribute> requestedGeneralAttributes = new HashSet<>(baseProductRequest.getAttributes());
            allAttributes.addAll(requestedGeneralAttributes);
            processProductAttributes(product, requestedGeneralAttributes);
        }
        Product newProduct = productRepository.save(product);

        List<ProductSku> productSkus = new ArrayList<>();
        if (baseProductRequest.getProductSkus() != null && !baseProductRequest.getProductSkus().isEmpty()) {
            Set<ProductAttribute> allSkuAttributes = new HashSet<>();
            for (BaseProductSkuRequest skuRequest : baseProductRequest.getProductSkus()) {
                ProductSku productSku = productMapper.toSkuEntity(skuRequest, newProduct);

                if (skuRequest.getAttributes() != null && !skuRequest.getAttributes().isEmpty()) {
                    Set<ProductAttribute> requestedSkuAttributes = new HashSet<>(skuRequest.getAttributes());
                    allSkuAttributes.addAll(requestedSkuAttributes);
                    processSkuAttributes(productSku, requestedSkuAttributes);
                }
                productSkus.add(productSku);
            }
            Map<String, ProductAttribute> skuAttributesMap = allSkuAttributes.stream()
                    .collect(Collectors.toMap(ProductAttribute::getAttributeKeyName,
                            attr -> ProductAttribute.builder()
                                    .attributeKeyName(attr.getAttributeKeyName())
                                    .attributeKeyDisplay(attr.getAttributeKeyDisplay())
                                    .values(new ArrayList<>(attr.getValues()))
                                    .build(), (oldA, newA) -> {
                                oldA.getValues().addAll(newA.getValues());
                                return oldA;
                            }));
            allAttributes.addAll(skuAttributesMap.values());
        }
        if(!productSkus.isEmpty())
            productSkuRepository.saveAll(productSkus);

        EsProductDto esProductDto = productMapper.toEsProductDto(newProduct, allAttributes);
        ProductSyncMessage message = ProductSyncMessage.builder().action(ProductSyncActionType.CREATE).product(esProductDto).build();
        productKafkaProducer.sendProductSyncMessage(message);
    }

    @Override
    public List<String> suggestMyProductsName(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
            return Collections.emptyList();
        }
//        Shop currentUserShop = securityUtils.getCurrentUserShop();
        Shop currentUserShop = shopRepository.findById("shop-0c6a-1e3a-aa7b-4f10920bd9f0")
                .orElseThrow(() -> new BadRequestException("SHOP_NOT_FOUND"));
        return productIndexerService.suggestMyProductsName(keyword, currentUserShop.getId());
    }

    @Override
    public List<ProductStatusDto> getFilterMeta() {
//        Shop currentUserShop = securityUtils.getCurrentUserShop();
        Shop currentUserShop = shopRepository.findById("shop-0c6a-1e3a-aa7b-4f10920bd9f0")
                .orElseThrow(() -> new BadRequestException("SHOP_NOT_FOUND"));

        List<ProductStatusDto> dataResult = productRepository.getMetaSumMyProductByStatus(currentUserShop.getId())
                .stream().map(s ->
                        ProductStatusDto.builder()
                                .id(s.getId())
                                .name(RestrictStatus.valueOf(s.getId()).getName())
                                .count(s.getCount()).build())
                .toList();
        int totalCount = dataResult.stream()
                .mapToInt(ProductStatusDto::getCount)
                .sum();

        List<ProductStatusDto> result = new ArrayList<>(dataResult);
        result.add(ProductStatusDto.builder()
                .id("ALL")
                .name("Tất cả")
                .count(totalCount)
                .build());

        return result;
    }

    @Override
    public MyShopProductListResponse getMyShopProducts(int page, int limit, String status, String search, String categoryId, String sortBy) {
        Shop currentUserShop = shopRepository.findById("shop-0c6a-1e3a-aa7b-4f10920bd9f0")
                .orElseThrow(() -> new BadRequestException("SHOP_NOT_FOUND"));

        Category category = null;
        if(categoryId != null && !categoryId.isEmpty()) {
            category = categoryService.findById(categoryId);
        }
        RestrictStatus restrictStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                restrictStatus = RestrictStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("RESTRICT_STATUS_INVALID");
            }
        }

        Specification<Product> spec = ProductSpecification.myShopFilter(search, category == null ? null : category.getId(), currentUserShop.getId(), restrictStatus);
        Pageable pageable = PageRequest.of(page, limit, ApplicationInitHelper.getSortBy(sortBy));

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<String> productIds = productPage.getContent().stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<ProductSku> productSkus = productSkuRepository.findByProductIdIn(productIds);

        return MyShopProductListResponse.builder().products(productPage.getContent().stream().map(p -> productMapper.toMyShopProductDto(p, productSkus.stream().filter(ps -> ps.getProduct().equals(p)).toList())).toList())
                .currentPage(productPage.getPageable().getPageNumber() + 1)
                .totalPages(productPage.getTotalPages())
                .totalCount(productPage.getTotalElements())
                .build();
    }


    private void processProductAttributes(Product product, Set<ProductAttribute> requestedAttributes) {
        Set<String> allRequestedAttributeKeyNames = requestedAttributes.stream()
                .map(ProductAttribute::getAttributeKeyName)
                .collect(Collectors.toSet());

        Map<String, AttributeKey> existingAttributeKeysMap = new HashMap<>();
        if (!allRequestedAttributeKeyNames.isEmpty()) {
            attributeKeyRepository.findByKeyNameIn(allRequestedAttributeKeyNames.stream().toList())
                    .forEach(ak -> existingAttributeKeysMap.put(ak.getKeyName(), ak));
        }

        Set<AbstractMap.SimpleEntry<String, String>> allRequestedKeyValuePairs = requestedAttributes.stream()
                .flatMap(attrDto -> attrDto.getValues().stream()
                        .map(val -> new AbstractMap.SimpleEntry<>(attrDto.getAttributeKeyName(), val)))
                .collect(Collectors.toSet());

        Map<AbstractMap.SimpleEntry<String, String>, AttributeValue> existingAttributeValuesMap = getOrCreateAttributeValues(
                allRequestedKeyValuePairs, existingAttributeKeysMap
        );

        for (ProductAttribute attrReq : requestedAttributes) {
            AttributeKey attributeKey = existingAttributeKeysMap.get(attrReq.getAttributeKeyName());
            if (attributeKey == null || attributeKey.isForSku()) {
                log.warn("AttributeKey '{}' ko tim thay.", attrReq.getAttributeKeyName());
                continue;
            }

            for (String value : attrReq.getValues()) {
                AbstractMap.SimpleEntry<String, String> keyValuePair = new AbstractMap.SimpleEntry<>(attrReq.getAttributeKeyName(), value); //
                AttributeValue attributeValue = existingAttributeValuesMap.get(keyValuePair);
                if (attributeValue == null) {
                    continue;
                }
                product.addGeneralAttribute(attributeValue);
            }
        }
    }

    private void processSkuAttributes(ProductSku productSku, Set<ProductAttribute> requestedAttributes) {
        Set<String> allRequestedAttributeKeyNames = requestedAttributes.stream()
                .map(ProductAttribute::getAttributeKeyName)
                .collect(Collectors.toSet());

        Map<String, AttributeKey> existingAttributeKeysMap = new HashMap<>();
        if (!allRequestedAttributeKeyNames.isEmpty()) {
            attributeKeyRepository.findByKeyNameIn(allRequestedAttributeKeyNames.stream().toList())
                    .forEach(ak -> existingAttributeKeysMap.put(ak.getKeyName(), ak));
        }

        Set<AbstractMap.SimpleEntry<String, String>> allRequestedKeyValuePairs = requestedAttributes.stream()
                .map(attrDto -> new AbstractMap.SimpleEntry<>(attrDto.getAttributeKeyName(), attrDto.getValues().get(0)))
                .collect(Collectors.toSet());

        Map<AbstractMap.SimpleEntry<String, String>, AttributeValue> existingAttributeValuesMap = getOrCreateAttributeValues(
                allRequestedKeyValuePairs, existingAttributeKeysMap
        );

        for (ProductAttribute attrReq : requestedAttributes) {
            AttributeKey attributeKey = existingAttributeKeysMap.get(attrReq.getAttributeKeyName());
            if (attributeKey == null || !attributeKey.isForSku()) {
                continue;
            }

            AbstractMap.SimpleEntry<String, String> keyValuePair = new AbstractMap.SimpleEntry<>(attrReq.getAttributeKeyName(), attrReq.getValues().get(0));
            AttributeValue attributeValue = existingAttributeValuesMap.get(keyValuePair);
            if (attributeValue == null) {
                continue;
            }
            productSku.getAttributes().add(attributeValue);
        }
    }

    private Map<AbstractMap.SimpleEntry<String, String>, AttributeValue> getOrCreateAttributeValues(
            Set<AbstractMap.SimpleEntry<String, String>> requestedKeyValuePairs,
            Map<String, AttributeKey> attributeKeysMap) {
        Map<AbstractMap.SimpleEntry<String, String>, AttributeValue> existingAttributeValuesMap = new HashMap<>();

        if (!requestedKeyValuePairs.isEmpty()) {
            Map<String, List<String>> valuesByAttributeKeyName = requestedKeyValuePairs.stream()
                    .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())));

            for (Map.Entry<String, List<String>> entry : valuesByAttributeKeyName.entrySet()) {
                String keyName = entry.getKey();
                List<String> values = entry.getValue();
                AttributeKey attributeKey = attributeKeysMap.get(keyName);
                if (attributeKey != null) {
                    attributeValueRepository.findByAttributeKeyAndValueIn(attributeKey, values)
                            .forEach(av -> existingAttributeValuesMap.put(new AbstractMap.SimpleEntry<>(keyName, av.getValue()), av)); //
                }
            }
        }

        List<AttributeValue> newAttributeValues = new ArrayList<>();
        for (AbstractMap.SimpleEntry<String, String> keyValuePair : requestedKeyValuePairs) {
            if (!existingAttributeValuesMap.containsKey(keyValuePair)) {
                String keyName = keyValuePair.getKey();
                String value = keyValuePair.getValue();

                AttributeKey attributeKey = attributeKeysMap.get(keyName);
                if (attributeKey == null) {
                    log.warn("AttributeKey '{}' not found for value '{}'. Cannot create AttributeValue.", keyName, value);
                    continue;
                }
                AttributeValue newAv = AttributeValue.builder()
                        .attributeKey(attributeKey)
                        .value(value)
                        .build();
                newAttributeValues.add(newAv);
            }
        }
        if (!newAttributeValues.isEmpty()) {
            List<AttributeValue> savedNewAttributeValues = attributeValueRepository.saveAll(newAttributeValues);
            savedNewAttributeValues.forEach(av -> existingAttributeValuesMap.put(new AbstractMap.SimpleEntry<>(av.getAttributeKey().getDisplayName(), av.getValue()), av)); //
        }
        return existingAttributeValuesMap;
    }
}