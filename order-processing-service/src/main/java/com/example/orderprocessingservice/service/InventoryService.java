package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.InventoryMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryService.class);

    public void handleNewInventory(InventoryMessage message) {
        LOGGER.info("Processing new inventory for product: {}, SKU: {}, Quantity: {}, Location: {}",
                message.getProductName(),
                message.getSku(),
                message.getQuantity(),
                message.getWarehouseLocation());
    }

    public void handleDeleteInventory(String id) {
        LOGGER.info("Processing inventory deletion for ID: {}", id);
    }

}

