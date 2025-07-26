package com.haiemdavang.AnrealShop.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.haiemdavang.AnrealShop.dto.product.EsProductDto;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsProduct;
import com.haiemdavang.AnrealShop.elasticsearch.repository.EsProductRepository;
import com.haiemdavang.AnrealShop.exception.AnrealShopException;
import com.haiemdavang.AnrealShop.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductIndexerService {
    private final EsProductRepository esProductRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductMapper productMapper;

    @Transactional
    public void indexProduct(EsProductDto message) {
        EsProduct esProduct =  productMapper.toEsProduct(message);
        esProductRepository.save(esProduct);
    }

    public List<EsProduct> suggestMyProductsName(String keyword, String id) {
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(QueryBuilders.bool(b -> {
                            b.should(QueryBuilders.matchPhrasePrefix(m ->
                                    m.field("name.suggest").query(keyword)));
                            b.should(QueryBuilders.match(m ->
                                    m.field("name.search_name").query(keyword)));

                            b.minimumShouldMatch("1");

                            if (id != null && !id.isBlank()) {
                                b.filter(f -> f.term(t -> t.field("shop.id").value(id)));
                            }
                            return b;
                        }
                ))
                .withMaxResults(10)
                .build();

        SearchHits<EsProduct> searchHit = elasticsearchTemplate.search(searchQuery, EsProduct.class);
        return searchHit.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }

    @Transactional
    public void deleteProductFromIndex(String id) {
        esProductRepository.deleteById(id);
    }

    @Transactional
    public void deleteProductFromIndex(Set<String> id) {
        esProductRepository.deleteByIdIn(id);
    }

    @Transactional
    public void updateProductVisibility(String id, boolean visible) {
        EsProduct esProduct = esProductRepository.findById(id)
                .orElseThrow(() -> new AnrealShopException("ES_PRODUCT_NOT_FOUND"));
        esProduct.setVisible(visible);
        esProduct.setUpdatedAt(Instant.now());
        esProductRepository.save(esProduct);
    }

    @Transactional
    public void updateProductVisibility(Set<String> ids, boolean visible) {
        Set<EsProduct> esProducts = esProductRepository.findByIdIn(ids);
        esProducts.forEach(esProduct -> {
            esProduct.setVisible(visible);
            esProduct.setUpdatedAt(Instant.now());
        });
        esProductRepository.saveAll(esProducts);
    }
}
