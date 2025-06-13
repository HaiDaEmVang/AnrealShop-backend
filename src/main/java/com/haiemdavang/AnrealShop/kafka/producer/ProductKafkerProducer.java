package com.haiemdavang.AnrealShop.kafka.producer;

import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductKafkerProducer {
    private final KafkaTemplate<String, ProductSyncMessage> kafkaTemplate;

    public void sendProductSyncMessage(ProductSyncMessage productSyncMessage) {
        log.info("Sending product sync message: {}", productSyncMessage);
//        kafkaTemplate.send("product-sync-topic", productSyncMessage.getProduct().getId(), productSyncMessage);
    }
}
