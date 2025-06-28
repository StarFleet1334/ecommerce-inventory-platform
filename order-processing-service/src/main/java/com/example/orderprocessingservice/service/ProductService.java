package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.ProductMP;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;


    public void handleNewProduct(ProductMP product) {

    }

    @Transactional
    public void handleDeleteProduct(String id) {
        LOGGER.info("Processing product deletion for ID: {}", id);

    }

}
