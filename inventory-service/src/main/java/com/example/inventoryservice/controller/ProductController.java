package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.messages.ProductMessage;
import com.example.inventoryservice.service.ProductService;
import com.example.inventoryservice.skeleton.ProductControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Override
    public ResponseEntity<String> deleteProductById(String id) {
        LOGGER.info("Received request to delete product with id: {}", id);
        productService.sendProductDeleteMessage(id);
        LOGGER.info("Product deleted successfully");
        return ResponseEntity.ok("Product deletion message successfully sent to the queue");
    }

    @Override
    public ResponseEntity<String> postProduct(ProductMessage productMessage, boolean initialLoad) {
        LOGGER.info("Received request to create product: {}", productMessage);
        if (initialLoad) {
            productService.sendProductInitialCreateMessage(productMessage);
            LOGGER.info("Initial product creation message successfully sent to the queue");
        } else {
            productService.sendProductCreateMessage(productMessage);
            LOGGER.info("Product creation message successfully sent to the queue");
        }
        return ResponseEntity.ok("Product creation message successfully sent to the queue");
    }
}
