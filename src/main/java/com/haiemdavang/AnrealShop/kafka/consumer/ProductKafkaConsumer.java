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
        log.info("Received message from Kafka: Action = {}, Product ID = {}",
                message.getAction(), message.getProduct());
        productIndexerService.indexProduct(message);
    }
}
