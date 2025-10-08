package com.haiemdavang.AnrealShop.tech.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    public static final String PRODUCT_SYNC_TOPIC = "topic-product-sync";
    public static final String NOTICE_SYNC_TOPIC = "topic-notice-sync";

    @Bean
    public NewTopic productSyncTopic(){
        return TopicBuilder.name(PRODUCT_SYNC_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic noticeSyncTopic(){
        return TopicBuilder.name(NOTICE_SYNC_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    
}
