package com.haiemdavang.AnrealShop.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchIndexManager {

    private final ElasticsearchClient elasticsearchClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.createIndexIfNotExists("products", "es/products-index.json");
        this.createIndexIfNotExists("categories", "es/categories-index.json");
    }

    private void createIndexIfNotExists(String indexName, String mappingFilePath) {
        try {
            var existsRequest = new ExistsRequest.Builder().index(indexName).build();
            boolean indexExists = elasticsearchClient.indices().exists(existsRequest).value();

            if (!indexExists) {
                log.info("Index '{}' does not exist. Creating...", indexName);
                InputStream mappingInputStream = new ClassPathResource(mappingFilePath).getInputStream();
                CreateIndexRequest createIndexRequest = CreateIndexRequest.of(b -> b
                        .index(indexName)
                        .withJson(mappingInputStream)
                );
                elasticsearchClient.indices().create(createIndexRequest);
                log.info("Successfully created Elasticsearch index: {}", indexName);

            } else {
                log.info("Elasticsearch index '{}' already exists.", indexName);
            }
        } catch (Exception e) {
            log.error("Failed to create or check Elasticsearch index: {}", indexName, e);
        }
    }
}