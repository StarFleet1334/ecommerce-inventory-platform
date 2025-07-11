package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.messages.StockMessage;
import com.example.inventoryservice.service.StockService;
import com.example.inventoryservice.skeleton.StockControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController implements StockControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);
    private final StockService stockService;

    @Override
    public ResponseEntity<String> deleteStockById(String id) {
        LOGGER.info("Received request to delete stock with id: {}", id);
        stockService.sendStockDeleteMessage(id);
        LOGGER.info("Stock deleted successfully");
        return ResponseEntity.ok("Stock deletion message successfully sent to the queue");
    }

    @Override
    public ResponseEntity<String> postStock(StockMessage stockMessage, boolean initialLoad) {
        LOGGER.info("Received request to create stock: {}", stockMessage);
        LOGGER.info("Stock created successfully");
        if (initialLoad) {
            stockService.sendStockInitialCreateMessage(stockMessage);
            LOGGER.info("Initial stock creation message successfully sent to the queue");
        } else {
            stockService.sendStockCreateMessage(stockMessage);
            LOGGER.info("Stock creation message successfully sent to the queue");
        }
        return ResponseEntity.ok("Stock creation message successfully sent to the queue");
    }
}
