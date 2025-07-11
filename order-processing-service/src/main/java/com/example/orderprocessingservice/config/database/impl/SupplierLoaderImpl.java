package com.example.orderprocessingservice.config.database.impl;

import com.example.orderprocessingservice.config.database.loaders.SupplierLoader;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class SupplierLoaderImpl extends SupplierLoader {
    public SupplierLoaderImpl(ObjectMapper objectMapper, SupplierRepository repository) {
        super(objectMapper, repository);
    }
}
