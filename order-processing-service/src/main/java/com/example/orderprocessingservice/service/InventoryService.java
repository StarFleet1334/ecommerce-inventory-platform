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
        LOGGER.info("Handling inventory update for product: {} (SKU: {})",
                message.getProductName(), message.getSku());
    }
}

