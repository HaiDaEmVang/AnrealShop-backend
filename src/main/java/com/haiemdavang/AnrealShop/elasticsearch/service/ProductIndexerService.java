package com.haiemdavang.AnrealShop.elasticsearch.service;

import com.haiemdavang.AnrealShop.elasticsearch.repository.EsProductRepository;
import com.haiemdavang.AnrealShop.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductIndexerService {
    private final EsProductRepository esProductRepository;
    private final ProductMapper productMapper;



}
