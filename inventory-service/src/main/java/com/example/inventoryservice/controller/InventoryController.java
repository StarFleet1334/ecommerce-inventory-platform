package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.common.InventoryItem;
import com.example.inventoryservice.entity.response.InventoryItemResponse;
import com.example.inventoryservice.service.InventoryService;
import com.example.inventoryservice.skeleton.InventoryControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InventoryController implements InventoryControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;

    @Override
    public ResponseEntity<String> deleteInventoryItem(String id) {
        LOGGER.info("Received request to delete inventory item with id: {}", id);
        inventoryService.sendInventoryDeletedMessage(id);
        LOGGER.info("Inventory item deleted successfully");
        return ResponseEntity.ok("Item successfully deleted");
    }

    @Override
    public ResponseEntity<InventoryItemResponse> postInventoryItem(InventoryItem inventoryItem) {
        LOGGER.info("Received request to create inventory item: {}", inventoryItem);
        InventoryItemResponse response = inventoryService.sendInventoryCreatedMessage(inventoryItem);
        LOGGER.info("Inventory item created successfully");
        return ResponseEntity.ok(response);

    }

}
