package com.haiemdavang.AnrealShop.elasticsearch.service;

import com.haiemdavang.AnrealShop.elasticsearch.document.EsProduct;
import com.haiemdavang.AnrealShop.elasticsearch.repository.EsProductRepository;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncActionType;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncMessage;
import com.haiemdavang.AnrealShop.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductIndexerService {
    private final EsProductRepository esProductRepository;
    private final ProductMapper productMapper;

    public void indexProduct(ProductSyncMessage message) {
        if (message.getAction() == ProductSyncActionType.CREATE ||
                message.getAction() == ProductSyncActionType.UPDATE) {
            EsProduct esProduct =  productMapper.toEsProduct(message.getProduct());
            esProductRepository.save(esProduct);
        } else if (message.getAction() == ProductSyncActionType.DELETE) {
            esProductRepository.deleteById(message.getProduct().getId());
        }
    }

}
