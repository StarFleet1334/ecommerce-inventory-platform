package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.service.ProductService;
import com.example.orderprocessingservice.skeleton.entity.ProductEntityControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductEntityController implements ProductEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEntityController.class);
    private final ProductService productService;

    @Override
    public ResponseEntity<Product> getProductById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Product>> getAllProduct() {
        return null;
    }
}
