package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.ProductMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.validator.ProductValidator;
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
    private final ProductValidator productValidator;


    public void handleNewProduct(ProductMP product) {
        LOGGER.info("Processing new product: {}", product);
        productValidator.validate(product);
        Product newProduct = mapToProduct(product);

        try {
            productRepository.save(newProduct);
            LOGGER.info("Successfully saved new product with name: {}", product.getProduct_name());
        } catch (Exception e) {
            LOGGER.error("Failed to save product: {}", e.getMessage());
            throw new RuntimeException("Failed to save product", e);
        }
    }

    @Transactional
    public void handleDeleteProduct(String id) {
        LOGGER.info("Processing product deletion for ID: {}", id);
        // Deletion of a Product is not supported at the moment
    }

    private Product mapToProduct(ProductMP product) {
        return Product.builder()
                .product_name(product.getProduct_name())
                .sku(product.getSku())
                .product_id(product.getProduct_id())
                .product_price(product.getProduct_price())
                .product_description(product.getProduct_description())
                .build();
    }

}
