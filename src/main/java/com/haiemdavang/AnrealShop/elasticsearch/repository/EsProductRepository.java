package com.haiemdavang.AnrealShop.elasticsearch.repository;

import com.haiemdavang.AnrealShop.elasticsearch.document.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long> {

}
