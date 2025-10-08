package com.haiemdavang.AnrealShop.tech.kafka.consumer;

import com.haiemdavang.AnrealShop.tech.elasticsearch.service.ProductIndexerService;
import com.haiemdavang.AnrealShop.tech.kafka.config.KafkaTopicConfig;
import com.haiemdavang.AnrealShop.tech.kafka.dto.ProductSyncMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductKafkaConsumer {
    private final ProductIndexerService productIndexerService;

    @KafkaListener(topics = KafkaTopicConfig.PRODUCT_SYNC_TOPIC)
    public void listen(ProductSyncMessage message) {
        switch (message.getAction()) {
            case CREATE, UPDATE -> productIndexerService.indexProduct(message.getProduct());
            case DELETE -> productIndexerService.deleteProductFromIndex(message.getId());
            case MULTI_DELETE -> productIndexerService.deleteProductFromIndex(message.getIds());
            case PRODUCT_UPDATED_VISIBILITY -> productIndexerService.updateProductVisibility(message.getId(), message.isVisible());
            case PRODUCT_UPDATE_MULTI_VISIBILITY ->  productIndexerService.updateProductVisibility(message.getIds(), message.isVisible());
            case PRODUCT_UPDATED_STATUS -> productIndexerService.updateProductStatus(message.getId(), message.getStatus());
            default -> log.warn("Unknown action type: {}", message.getAction());
        }

    }
}
