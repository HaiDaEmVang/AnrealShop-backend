package com.haiemdavang.AnrealShop.kafka.producer;

import com.haiemdavang.AnrealShop.kafka.config.KafkaTopicConfig;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductKafkaProducer {
    private final KafkaTemplate<String, ProductSyncMessage> kafkaTemplate;

    public void sendProductSyncMessage(ProductSyncMessage productSyncMessage) {
        kafkaTemplate.send(KafkaTopicConfig.PRODUCT_SYNC_TOPIC, productSyncMessage.getId(), productSyncMessage);
    }
}
