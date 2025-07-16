package com.haiemdavang.AnrealShop.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsProduct;
import com.haiemdavang.AnrealShop.elasticsearch.repository.EsProductRepository;
import com.haiemdavang.AnrealShop.exception.AnrealShopException;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncActionType;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncMessage;
import com.haiemdavang.AnrealShop.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductIndexerService {
    private final EsProductRepository esProductRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductMapper productMapper;

    @Transactional
    public void indexProduct(ProductSyncMessage message) {
        EsProduct esProduct =  productMapper.toEsProduct(message.getProduct());
        esProductRepository.save(esProduct);
    }

    public List<String> suggestMyProductsName(String keyword, String id) {
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(QueryBuilders.bool(b -> b
                        .should(QueryBuilders.matchPhrasePrefix(m ->
                                m.field("name.suggest").query(keyword)))
                        .should(QueryBuilders.match(m ->
                                m.field("name.search_name").query(keyword)))
                        .filter(f -> f
                                .term(t -> t
                                        .field("shop.id")
                                        .value(id)))
                        .minimumShouldMatch("1")
                ))
                .withMaxResults(10)
                .build();

        SearchHits<EsProduct> searchHit = elasticsearchTemplate.search(searchQuery, EsProduct.class);
        return searchHit.getSearchHits().stream()
                .map(hit -> hit.getContent().getName())
                .toList();
    }

    @Transactional
    public void deleteProductFromIndex(String id) {
        esProductRepository.deleteById(id);
    }

    @Transactional
    public void updateProductVisibility(String productId, boolean visible) {
        EsProduct esProduct = esProductRepository.findById(productId)
                .orElseThrow(() -> new AnrealShopException("ES_PRODUCT_NOT_FOUND"));
        esProduct.setVisible(visible);
        esProduct.setUpdatedAt(Instant.now());
        esProductRepository.save(esProduct);
    }
}
