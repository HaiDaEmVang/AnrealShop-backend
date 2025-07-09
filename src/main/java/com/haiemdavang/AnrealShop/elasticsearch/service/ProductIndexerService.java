package com.haiemdavang.AnrealShop.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsProduct;
import com.haiemdavang.AnrealShop.elasticsearch.repository.EsProductRepository;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncActionType;
import com.haiemdavang.AnrealShop.kafka.dto.ProductSyncMessage;
import com.haiemdavang.AnrealShop.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductIndexerService {
    private final EsProductRepository esProductRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
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

    public List<String> suggestMyProductsName(String keyword, String id) {
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(
                        QueryBuilders.bool(b -> b
                                .must(m -> m
                                        .match(match -> match
                                            .field("name.suggest")
                                            .operator(Operator.And)
                                            .analyzer("vietnamese_analyzer")
                                            .fuzziness("AUTO")
                                            .query(keyword)))
                                .filter(f -> f
                                    .term(t -> t
                                            .field("shop.id")
                                            .value(id)))))

                .withMaxResults(10)
                .build();
        SearchHits<EsProduct> searchHit = elasticsearchTemplate.search(searchQuery, EsProduct.class);
        return searchHit.getSearchHits().stream()
                .map(hit -> hit.getContent().getName())
                .toList();
    }

}
