package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.attribute.ProductAttributeDto;
import com.haiemdavang.AnrealShop.dto.product.*;
import com.haiemdavang.AnrealShop.elasticsearch.service.ProductIndexerService;
import com.haiemdavang.AnrealShop.exception.BadRequestException;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncActionType;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncMessage;
import com.haiemdavang.AnrealShop.kafka.producer.ProductKafkaProducer;
import com.haiemdavang.AnrealShop.mapper.AttributeMapper;
import com.haiemdavang.AnrealShop.mapper.ProductMapper;
import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeKey;
import com.haiemdavang.AnrealShop.modal.entity.attribute.AttributeValue;
import com.haiemdavang.AnrealShop.modal.entity.category.Category;
import com.haiemdavang.AnrealShop.modal.entity.product.Product;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductGeneralAttribute;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductMedia;
import com.haiemdavang.AnrealShop.modal.entity.product.ProductSku;
import com.haiemdavang.AnrealShop.modal.entity.shop.Shop;
import com.haiemdavang.AnrealShop.modal.enums.MediaType;
import com.haiemdavang.AnrealShop.modal.enums.RestrictStatus;
import com.haiemdavang.AnrealShop.repository.AttributeKeyRepository;
import com.haiemdavang.AnrealShop.repository.AttributeValueRepository;
import com.haiemdavang.AnrealShop.repository.ProductGeneralAttributeRepository;
import com.haiemdavang.AnrealShop.repository.ProductSkuRepository;
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
    private final ProductGeneralAttributeRepository productGeneralAttributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeKeyRepository attributeKeyRepository;
    private final ProductMapper productMapper;

    private final ProductKafkaProducer productKafkaProducer;
    private final ProductIndexerService productIndexerService;

    private final SecurityUtils securityUtils;

    private final AttributeMapper attributeMapper;
    private final AttributeServiceImp attributeServiceImp;

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElse(null);
    }

    @Override
    public Product getProductByIdAndThrow(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("PRODUCT_NOT_FOUND"));
    }


    @Override
    @Transactional
    public void createProduct(BaseProductRequest baseProductRequest) {
        Shop currentUserShop = securityUtils.getCurrentUserShop();

        Category category = categoryService.findByIdAndThrow(baseProductRequest.getCategoryId());

        Product product = productMapper.toEntity(baseProductRequest, category, currentUserShop);

        this.updateProductMediaList(baseProductRequest.getMedia(), product);

        List<ProductAttributeDto> allAttributes = new ArrayList<>();


        if (baseProductRequest.getAttributes() != null && !baseProductRequest.getAttributes().isEmpty()) {
            Set<ProductAttributeDto> requestedGeneralAttributes = new HashSet<>(baseProductRequest.getAttributes());
            allAttributes.addAll(requestedGeneralAttributes);
            processProductAttributes(product, requestedGeneralAttributes);
        }
        Product newProduct = productRepository.save(product);

        List<ProductSku> productSkus = new ArrayList<>();
        if (baseProductRequest.getProductSkus() != null && !baseProductRequest.getProductSkus().isEmpty()) {
            Set<ProductAttributeDto> allSkuAttributes = new HashSet<>();
            for (BaseProductSkuRequest skuRequest : baseProductRequest.getProductSkus()) {
                ProductSku productSku = productMapper.toSkuEntity(skuRequest, newProduct);

                if (skuRequest.getAttributes() != null && !skuRequest.getAttributes().isEmpty()) {
                    Set<ProductAttributeDto> requestedSkuAttributes = new HashSet<>(skuRequest.getAttributes());
                    allSkuAttributes.addAll(requestedSkuAttributes);
                    processSkuAttributes(productSku, requestedSkuAttributes);
                }
                productSkus.add(productSku);
            }
            Map<String, ProductAttributeDto> skuAttributesMap = allSkuAttributes.stream()
                    .collect(Collectors.toMap(ProductAttributeDto::getAttributeKeyName,
                            attr -> ProductAttributeDto.builder()
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
    @Transactional
    public MyShopProductDto updateProduct(String id, BaseProductRequest baseProductRequest) {
        Product product = productRepository.findWithCategoryAndMediaAndGeneralAttributeById(id)
                .orElseThrow(() -> new BadRequestException("PRODUCT_NOT_FOUND"));

        Category category = null;
        if (baseProductRequest.getCategoryId() != null && categoryService.existsById(baseProductRequest.getCategoryId())) {
            if (product.getCategory() != null && !product.getCategory().getId().equals(baseProductRequest.getCategoryId())) {
                category = categoryService.getReferenceById(baseProductRequest.getCategoryId());
            }
        } else {
            throw new BadRequestException("CATEGORY_NOT_FOUND");
        }
        productMapper.updateEntity(product, baseProductRequest, category);

        this.updateProductMediaList(baseProductRequest.getMedia(), product);

        List<ProductGeneralAttribute> oldAttributeForProduct = productGeneralAttributeRepository.findProductGeneralAttributesByProductId(id);

        this.updateAttributeForProduct(oldAttributeForProduct, baseProductRequest.getAttributes(), product);

        List<ProductSku> oldProductSkus = productSkuRepository.findWithAttributeByProductId(id);

        List<ProductAttributeDto> attributeList = new ArrayList<>(baseProductRequest.getAttributes());
        this.updateProductSkus(oldProductSkus, baseProductRequest.getProductSkus(), product);

        productRepository.save(product);

        ProductSyncMessage message = ProductSyncMessage.builder()
                .action(ProductSyncActionType.UPDATE)
                .productId(product.getId())
                .product(productMapper.toEsProductDto(product, attributeMapper.formatAttributes(attributeList)))
                .build();
        productKafkaProducer.sendProductSyncMessage(message);
        return null;
    }

    @Override
    @Transactional
    public MyShopProductDto updateProductVisible(String id, boolean visible) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("PRODUCT_NOT_FOUND"));
        product.setVisible(visible);
        productRepository.save(product);
        ProductSyncMessage message = ProductSyncMessage.builder()
                .action(ProductSyncActionType.PRODUCT_VISIBILITY_UPDATED)
                .productId(product.getId())
                .product(null)
                .build();
        productKafkaProducer.sendProductSyncMessage(message);
        return null;
    }

    @Override
    @Transactional
    public void delete(String id, boolean isForce) {
        if(productRepository.existsById(id)){
            if (isForce) {
                productRepository.deleteById(id);
                ProductSyncMessage message = ProductSyncMessage.builder()
                        .action(ProductSyncActionType.DELETE).productId(id).build();
                productKafkaProducer.sendProductSyncMessage(message);
            } else {
                Product product = getProductByIdAndThrow(id);
                product.setDeleted(true);
                product.setVisible(false);
                productRepository.save(product);
                List<ProductAttributeDto> attributeForProduct = attributeMapper
                        .toProductAttributeDto(productGeneralAttributeRepository.findProductAttributeByIdProduct(id));
                ProductSyncMessage message = ProductSyncMessage.builder()
                        .action(ProductSyncActionType.UPDATE)
                        .productId(product.getId())
                        .product(productMapper.toEsProductDto(product, attributeForProduct))
                        .build();
                productKafkaProducer.sendProductSyncMessage(message);
            }
        }
        else {
            throw new BadRequestException("PRODUCT_NOT_FOUND");
        }
    }

    @Override
    public List<String> suggestMyProductsName(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
            return Collections.emptyList();
        }
        Shop currentUserShop = securityUtils.getCurrentUserShop();

        return productIndexerService.suggestMyProductsName(keyword, currentUserShop.getId());
    }

    @Override
    public List<ProductStatusDto> getFilterMeta() {
        Shop currentUserShop = securityUtils.getCurrentUserShop();
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
        Shop currentUserShop = securityUtils.getCurrentUserShop();
        Category category = null;
        if(categoryId != null && !categoryId.isEmpty()) {
            category = categoryService.findByIdAndThrow(categoryId);
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

    private void updateAttributeForProduct(List<ProductGeneralAttribute> oldAttributeForProduct,
                                          List<ProductAttributeDto> attributes,
                                          Product product) {

        Set<AttributeValue> newAttributeValues = attributeServiceImp.getAttributeValues(attributes);

        Set<ProductGeneralAttribute> attributeToDelete = oldAttributeForProduct.stream()
                .filter(pga -> !newAttributeValues.contains(pga.getAttributeValue()))
                .collect(Collectors.toSet());
        product.getGeneralAttributes().removeAll(attributeToDelete);

        Set<AttributeValue> oldAttributeValues = oldAttributeForProduct.stream()
                .map(ProductGeneralAttribute::getAttributeValue)
                .collect(Collectors.toSet());

        for (AttributeValue av : newAttributeValues) {
            if (oldAttributeValues.contains(av)) {
                continue;
            }
            product.addGeneralAttribute(av);
        }
    }

    private void updateProductSkus(List<ProductSku> oldProductSkus, List<BaseProductSkuRequest> productSkus, Product product) {
        Map<String, BaseProductSkuRequest> newSkuMap = productSkus.stream()
                .collect(Collectors.toMap(BaseProductSkuRequest::getSku, sku -> sku));

        Map<String, ProductSku> oldSkuMap = oldProductSkus.stream()
                .collect(Collectors.toMap(ProductSku::getSku, sku -> sku));

        Set<String> toDeleteSkus = oldSkuMap.keySet().stream()
                .filter(sku -> !newSkuMap.containsKey(sku))
                .collect(Collectors.toSet());

        product.getProductSkus().removeIf(sku -> toDeleteSkus.contains(sku.getSku()));

        Set<ProductSku> skusToSave = new HashSet<>();

        for (BaseProductSkuRequest request : productSkus) {
            ProductSku existing = oldSkuMap.get(request.getSku());

            if (existing != null) {
                existing.setPrice(request.getPrice());
                existing.setQuantity(request.getQuantity());
                existing.setThumbnailUrl(request.getImageUrl());
                skusToSave.add(existing);
            } else {
                ProductSku newSku = productMapper.toSkuEntity(request, product);
                Set<AttributeValue> attributes = attributeServiceImp.getAttributeValues(request.getAttributes());
                newSku.getAttributes().addAll(attributes);
                product.getProductSkus().add(newSku);
                skusToSave.add(newSku);
            }
        }

        if (!skusToSave.isEmpty()) {
            productSkuRepository.saveAll(skusToSave);
        }
    }

    private void updateProductMediaList(List<ProductMediaDto> productMediaDto, Product product) {
        if (product.getMediaList() == null) {
            product.setMediaList(new HashSet<>());
        }

        Map<String, ProductMediaDto> newMediaDtoMap = productMediaDto.stream()
                .collect(Collectors.toMap(ProductMediaDto::getId,
                        media -> media,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));

        Set<ProductMedia> mediaToRemoves = product.getMediaList().stream()
                .filter(media -> !newMediaDtoMap.containsKey(media.getId()))
                .collect(Collectors.toSet());
        product.getMediaList().removeAll(mediaToRemoves);

        Map<String, ProductMedia> existingMediaMap = product.getMediaList().stream()
                .collect(Collectors.toMap(ProductMedia::getId, media -> media));

        Set<ProductMedia> mediaToAdd = new HashSet<>();
        for (Map.Entry<String, ProductMediaDto> entry : newMediaDtoMap.entrySet()) {
            String mediaId = entry.getKey();
            ProductMediaDto mediaDto = entry.getValue();

            ProductMedia existingMedia = existingMediaMap.get(mediaId);

            if (existingMedia == null) {
                ProductMedia newMedia = ProductMedia.builder()
                        .id(mediaId)
                        .url(mediaDto.getUrl())
                        .type(MediaType.valueOf(mediaDto.getType()))
                        .product(product)
                        .build();
                mediaToAdd.add(newMedia);
            }
        }
        product.getMediaList().addAll(mediaToAdd);

        productMediaDto.stream()
                .filter(media -> "IMAGE".equals(media.getType()))
                .findFirst()
                .ifPresent(media -> product.setThumbnailUrl(media.getUrl()));
    }

    private void processProductAttributes(Product product, Set<ProductAttributeDto> requestedAttributes) {
        Set<String> allRequestedAttributeKeyNames = requestedAttributes.stream()
                .map(ProductAttributeDto::getAttributeKeyName)
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

        for (ProductAttributeDto attrReq : requestedAttributes) {
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

    private void processSkuAttributes(ProductSku productSku, Set<ProductAttributeDto> requestedAttributes) {
        Set<String> allRequestedAttributeKeyNames = requestedAttributes.stream()
                .map(ProductAttributeDto::getAttributeKeyName)
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

        for (ProductAttributeDto attrReq : requestedAttributes) {
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