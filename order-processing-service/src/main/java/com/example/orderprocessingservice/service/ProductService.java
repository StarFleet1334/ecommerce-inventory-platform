package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.ProductMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.mapper.product.ProductMapper;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final ProductMapper productMapper;
    private final StockRepository stockRepository;
    private final WareHouseRepository wareHouseRepository;

    public void handleNewProduct(ProductMP product) {
        LOGGER.info("Processing new product: {}", product);
        productValidator.validate(product);
        Product newProduct = productMapper.toEntity(product);

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

        Product product = productRepository.findByProductId(id);
        if (product == null) {
            throw new IllegalArgumentException("Product with ID " + id + " does not exist");
        }
        List<Stock> stocksToDelete = stockRepository.findAllByProductId(id);
        for (Stock stock : stocksToDelete) {
            WareHouse wareHouse = stock.getWareHouse();
            wareHouse.setWareHouseCapacity(wareHouse.getWareHouseCapacity() - stock.getQuantity());
            wareHouseRepository.save(wareHouse);
            stockRepository.delete(stock);

        }
        productRepository.delete(product);
        LOGGER.info("Successfully deleted product with ID {} and its related stocks", id);

    }

}
