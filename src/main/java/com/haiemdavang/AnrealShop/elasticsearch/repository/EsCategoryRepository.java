package com.haiemdavang.AnrealShop.elasticsearch.repository;

import com.haiemdavang.AnrealShop.elasticsearch.document.EsCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsCategoryRepository extends ElasticsearchRepository<EsCategory, String> {
}
