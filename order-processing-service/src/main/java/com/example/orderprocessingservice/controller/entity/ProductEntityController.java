package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.exception.asset.ProductException;
import com.example.orderprocessingservice.service.ProductService;
import com.example.orderprocessingservice.skeleton.entity.ProductEntityControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductEntityController implements ProductEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEntityController.class);
    private final ProductService productService;

    @Override
    public ResponseEntity<Object> getProductById(String id) {
        try {
            LOGGER.info("Received request to retrieve product with ID: {}", id);
            Product product = productService.getProductById(id);
            LOGGER.info("Successfully retrieved product with ID: {}", id);
            return ResponseEntity.ok(product);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid product ID format: " + id);
        } catch (ProductException e) {
            LOGGER.warn("Product with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Product with ID " + id + " not found", "message", "Product not found. Please check the ID and try again. If the problem persists, contact your system administrator."));
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve product with ID: {}", id, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<List<Product>> getAllProduct() {
        try {
            List<Product> products = productService.getAllProducts();
            LOGGER.info("Successfully retrieved all products");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all products", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}
