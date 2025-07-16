package com.haiemdavang.AnrealShop.kafka.consumer;

import com.haiemdavang.AnrealShop.elasticsearch.service.ProductIndexerService;
import com.haiemdavang.AnrealShop.kafka.config.KafkaTopicConfig;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncMessage;
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
            case CREATE, UPDATE -> productIndexerService.indexProduct(message);
            case DELETE -> productIndexerService.deleteProductFromIndex(message.getProductId());
            case PRODUCT_VISIBILITY_UPDATED -> productIndexerService.updateProductVisibility(message.getProductId(), message.isVisible());
            default -> log.warn("Unknown action type: {}", message.getAction());
        }

    }
}
