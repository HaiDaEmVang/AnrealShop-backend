package com.haiemdavang.AnrealShop.tech.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.haiemdavang.AnrealShop.tech.elasticsearch.document.EsCategory;
import com.haiemdavang.AnrealShop.tech.elasticsearch.document.EsProduct;
import com.haiemdavang.AnrealShop.tech.elasticsearch.repository.EsCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryIndexerService {
    private final EsCategoryRepository esCategoryRepository;
    private final ProductIndexerService productIndexerService;
    private final ElasticsearchTemplate elasticsearchTemplate;

    public List<EsCategory> getCategoriesByKeyword(String keyword) {
        NativeQuery searchQuery;

        if (keyword == null || keyword.isBlank()) {
            Query query = Query.of(q -> q.matchAll(m -> m));
            searchQuery = NativeQuery.builder()
                    .withQuery(query)
                    .withMaxResults(5)
                    .build();
        } else {
            searchQuery = NativeQuery.builder()
                    .withQuery(QueryBuilders.bool(b -> b
                            .should(QueryBuilders.matchPhrasePrefix(m ->
                                    m.field("url_path.suggest").query(keyword)))
                            .should(QueryBuilders.match(m ->
                                    m.field("url_path.search_name").query(keyword)))
                    ))
                    .withMaxResults(5)
                    .build();
        }
        SearchHits<EsCategory> searchHit = elasticsearchTemplate.search(searchQuery, EsCategory.class);
        return searchHit.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }

    public Set<EsCategory> getCategoriesByProductName(String keyword, String idShop) {
        List<EsProduct> products = productIndexerService.suggestMyProductsName(keyword, idShop);
        Set<String> cateIds = products.stream().map(EsProduct::getCategoryId).collect(Collectors.toSet());
        return esCategoryRepository.findByIdIn(cateIds);
    }
}
