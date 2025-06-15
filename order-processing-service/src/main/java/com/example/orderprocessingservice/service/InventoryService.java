package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.InventoryMessage;
import com.example.orderprocessingservice.dto.model.Inventory;
import com.example.orderprocessingservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    public void handleNewInventory(InventoryMessage message) {
        LOGGER.info("Processing new inventory for product: {}, SKU: {}, Quantity: {}, Location: {}",
                message.getProductName(),
                message.getSku(),
                message.getQuantity(),
                message.getWarehouseLocation());
        Inventory inventory = Inventory.builder()
                .productId(message.getId())
                .productName(message.getProductName())
                .sku(message.getSku())
                .quantity(message.getQuantity())
                .warehouseLocation(message.getWarehouseLocation())
                .lastUpdated(message.getLastUpdated())
                .build();
        inventoryRepository.save(inventory);
        LOGGER.info("Inventory saved successfully");

    }

    @Transactional
    public void handleDeleteInventory(String id) {
        LOGGER.info("Processing inventory deletion for ID: {}", id);
        inventoryRepository.deleteByProductId(id);
        LOGGER.info("Inventory deleted successfully");
    }

}

