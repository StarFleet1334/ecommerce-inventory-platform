package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.ProductMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.exception.asset.ProductException;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final ProductMapper productMapper;
    private final StockRepository stockRepository;
    private final WareHouseRepository wareHouseRepository;

    @Transactional
    public void handleNewProduct(ProductMP productRequest) {
        LOGGER.info("Processing new product: {}", productRequest);
        validateProductRequest(productRequest);
        Product savedProduct = createAndSaveProduct(productRequest);
        LOGGER.info("Successfully processed new product with ID: {}", savedProduct.getProduct_id());
    }

    @Transactional
    public void handleDeleteProduct(String productId) {
        LOGGER.info("Processing product deletion for ID: {}", productId);
        // Step 1: Validating product exists
        Product product = validateAndGetProduct(productId);
        // Step 2: Getting all related stocks
        List<Stock> relatedStocks = getRelatedStocks(productId);
        // Step 3: Processing stock deletions and update warehouse capacities
        ProductDeletionResult deletionResult = processRelatedStockDeletions(relatedStocks);
        // Step 4: Deleting the product itself
        executeProductDeletion(product);
        LOGGER.info("Successfully deleted product with ID {} and {} related stocks. Total capacity freed: {}",
                productId, deletionResult.getDeletedStocksCount(), deletionResult.getTotalCapacityFreed());
    }

    public Product getProductById(String productId) {
        LOGGER.info("Fetching product with ID: {}", productId);
        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            LOGGER.error("Product not found with ID: {}", productId);
            throw ProductException.notFound(productId);
        }
        LOGGER.debug("Product found successfully with ID: {}", productId);
        return product;
    }

    public List<Product> getAllProducts() {
        LOGGER.info("Fetching all products");

        List<Product> products = productRepository.findAll();

        LOGGER.info("Successfully retrieved {} products", products.size());
        return products;
    }

    // Helper Methods ---------------------------------------------------------------------------------------

    private void validateProductRequest(ProductMP productRequest) {
        try {
            productValidator.validate(productRequest);
            LOGGER.debug("Product validation successful for: {}", productRequest.getProduct_name());
        } catch (Exception e) {
            LOGGER.error("Product validation failed: {}", e.getMessage());
            throw e;
        }
    }

    private Product createAndSaveProduct(ProductMP productRequest) {
        try {
            Product newProduct = productMapper.toEntity(productRequest);
            productRepository.save(newProduct);
            LOGGER.info("Successfully saved new product with name: {}", productRequest.getProduct_name());
            return newProduct;
        } catch (Exception e) {
            LOGGER.error("Failed to save product: {}", e.getMessage());
            throw new RuntimeException("Failed to save product", e);
        }
    }

    private Product validateAndGetProduct(String productId) {
        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            LOGGER.error("Product not found with ID: {}", productId);
            throw ProductException.notFound(productId);
        }
        return product;
    }

    private List<Stock> getRelatedStocks(String productId) {
        List<Stock> stocks = stockRepository.findAllByProductId(productId);
        LOGGER.info("Found {} related stocks for product ID: {}", stocks.size(), productId);
        return stocks;
    }

    private ProductDeletionResult processRelatedStockDeletions(List<Stock> stocks) {
        if (stocks.isEmpty()) {
            LOGGER.info("No related stocks to delete");
            return ProductDeletionResult.builder()
                    .deletedStocksCount(0)
                    .totalCapacityFreed(0)
                    .affectedWarehouses(0)
                    .build();
        }

        LOGGER.info("Processing deletion of {} related stocks", stocks.size());

        // Group stocks by warehouse for efficient processing
        Map<WareHouse, List<Stock>> stocksByWarehouse = stocks.stream()
                .collect(Collectors.groupingBy(Stock::getWareHouse));

        int totalCapacityFreed = 0;
        int deletedStocksCount = 0;

        for (Map.Entry<WareHouse, List<Stock>> entry : stocksByWarehouse.entrySet()) {
            WareHouse warehouse = entry.getKey();
            List<Stock> warehouseStocks = entry.getValue();

            int warehouseCapacityToFree = processWarehouseStockDeletion(warehouse, warehouseStocks);
            totalCapacityFreed += warehouseCapacityToFree;
            deletedStocksCount += warehouseStocks.size();
        }

        return ProductDeletionResult.builder()
                .deletedStocksCount(deletedStocksCount)
                .totalCapacityFreed(totalCapacityFreed)
                .affectedWarehouses(stocksByWarehouse.size())
                .build();
    }

    private int processWarehouseStockDeletion(WareHouse warehouse, List<Stock> stocks) {
        int totalQuantityToFree = stocks.stream()
                .mapToInt(Stock::getQuantity)
                .sum();

        int currentCapacity = warehouse.getWareHouseCapacity();
        int newCapacity = currentCapacity - totalQuantityToFree;
        warehouse.setWareHouseCapacity(newCapacity);

        try {
            wareHouseRepository.save(warehouse);
            LOGGER.debug("Updated warehouse {} capacity from {} to {} (freed: {})",
                    warehouse.getWareHouseId(), currentCapacity, newCapacity, totalQuantityToFree);
        } catch (Exception e) {
            LOGGER.error("Failed to update warehouse capacity for warehouse {}: {}",
                    warehouse.getWareHouseId(), e.getMessage());
            throw new RuntimeException("Failed to update warehouse capacity", e);
        }

        try {
            stockRepository.deleteAll(stocks);
            LOGGER.debug("Deleted {} stocks from warehouse {}", stocks.size(), warehouse.getWareHouseId());
        } catch (Exception e) {
            LOGGER.error("Failed to delete stocks from warehouse {}: {}",
                    warehouse.getWareHouseId(), e.getMessage());
            throw new RuntimeException("Failed to delete stocks", e);
        }

        return totalQuantityToFree;
    }

    private void executeProductDeletion(Product product) {
        try {
            productRepository.delete(product);
            LOGGER.info("Successfully deleted product with ID: {}", product.getProduct_id());
        } catch (Exception e) {
            LOGGER.error("Failed to delete product with ID {}: {}", product.getProduct_id(), e.getMessage());
            throw new RuntimeException("Failed to delete product with ID: " + product.getProduct_id(), e);
        }
    }

    @lombok.Builder
    @lombok.Data
    private static class ProductDeletionResult {
        private int deletedStocksCount;
        private int totalCapacityFreed;
        private int affectedWarehouses;
    }
}
