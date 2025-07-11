package com.example.orderprocessingservice.config.database.impl;

import com.example.orderprocessingservice.config.database.loaders.ProductLoader;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductLoaderImpl extends ProductLoader {
    public ProductLoaderImpl(ObjectMapper objectMapper, ProductRepository repository) {
        super(objectMapper, repository);
    }
}
