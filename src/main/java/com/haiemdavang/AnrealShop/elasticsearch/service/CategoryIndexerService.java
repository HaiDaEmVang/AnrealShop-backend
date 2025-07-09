package com.haiemdavang.AnrealShop.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.haiemdavang.AnrealShop.elasticsearch.document.EsCategory;
import com.haiemdavang.AnrealShop.elasticsearch.repository.EsCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryIndexerService {
    private final EsCategoryRepository esCategoryRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    public List<EsCategory> getCategoriesByKeyword(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return null;
        }
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(QueryBuilders.match(
                        m -> m.field("name.suggest")
                                .query(keyword)
                                .analyzer("vietnamese_analyzer")
                                .fuzziness("AUTO")
                                .operator(Operator.And)
                ))
                .withMaxResults(10)
                .build();
        SearchHits<EsCategory> searchHit = elasticsearchTemplate.search(searchQuery, EsCategory.class);
        return searchHit.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }

    public void indexCategory(EsCategory esCategory) {
        esCategoryRepository.save(esCategory);
    }
}
