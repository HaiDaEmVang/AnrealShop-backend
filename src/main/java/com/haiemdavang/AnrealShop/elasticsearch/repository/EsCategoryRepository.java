package com.haiemdavang.AnrealShop.elasticsearch.repository;

import com.haiemdavang.AnrealShop.elasticsearch.document.EsCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface EsCategoryRepository extends ElasticsearchRepository<EsCategory, String> {
    Set<EsCategory> findByIdIn(Collection<String> ids);
}
