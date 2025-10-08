package com.haiemdavang.AnrealShop.tech.elasticsearch.repository;

import com.haiemdavang.AnrealShop.tech.elasticsearch.document.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface EsProductRepository extends ElasticsearchRepository<EsProduct, String> {

    void deleteByIdIn(Collection<String> ids);

    Set<EsProduct> findByIdIn(Collection<String> ids);
}
