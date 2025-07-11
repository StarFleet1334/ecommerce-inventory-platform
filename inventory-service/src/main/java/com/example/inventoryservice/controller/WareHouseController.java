package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.messages.WareHouseMessage;
import com.example.inventoryservice.service.WareHouseService;
import com.example.inventoryservice.skeleton.WareHouseControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WareHouseController implements WareHouseControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(WareHouseController.class);
    private final WareHouseService wareHouseService;

    @Override
    public ResponseEntity<String> deleteWareHouseById(String id) {
        LOGGER.info("Received request to delete warehouse with id: {}", id);
        wareHouseService.sendWareHouseDeleteMessage(id);
        LOGGER.info("WareHouse deleted successfully");
        return ResponseEntity.ok("WareHouse deletion message successfully sent to the queue");
    }

    @Override
    public ResponseEntity<String> postWareHouse(WareHouseMessage wareHouseMessage, boolean initialLoad) {
        LOGGER.info("Received request to create warehouse: {}", wareHouseMessage);
        if (initialLoad) {
            wareHouseService.sendWareHouseInitialCreateMessage(wareHouseMessage);
            LOGGER.info("Initial warehouse creation message successfully sent to the queue");
        } else {
            wareHouseService.sendWareHouseCreateMessage(wareHouseMessage);
            LOGGER.info("WareHouse creation message successfully sent to the queue");
        }
        return ResponseEntity.ok("WareHouse creation message successfully sent to the queue");
    }
}
