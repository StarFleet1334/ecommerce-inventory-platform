package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.messages.SupplyMessage;
import com.example.inventoryservice.service.SupplyService;
import com.example.inventoryservice.skeleton.SupplyControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SupplyController implements SupplyControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplyController.class);
    private final SupplyService supplyService;

    @Override
    public ResponseEntity<String> postSupplier(SupplyMessage supplyMessage) {
        LOGGER.info("Received request to create supply: {}", supplyMessage);
        supplyService.sendSupplyCreateMessage(supplyMessage);
        LOGGER.info("Supply created successfully");
        return ResponseEntity.ok("Supply creation message successfully sent to the queue");
    }

    @Override
    public ResponseEntity<String> deleteSupplyById(String id) {
        LOGGER.info("Received request to delete supply with id: {}", id);
        supplyService.sendSupplyDeleteMessage(id);
        LOGGER.info("Supply deleted successfully");
        return ResponseEntity.ok("Supply deletion message successfully sent to the queue");
    }
}
