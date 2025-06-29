package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.messages.SupplierMessage;
import com.example.inventoryservice.service.SupplierService;
import com.example.inventoryservice.skeleton.SupplierControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SupplierController implements SupplierControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierController.class);
    private final SupplierService supplierService;

    @Override
    public ResponseEntity<String> deleteSupplierById(String id) {
        LOGGER.info("Received request to delete supplier with id: {}", id);
        supplierService.sendSupplierDeleteMessage(id);
        LOGGER.info("Supplier deleted successfully");
        return ResponseEntity.ok("Supplier deletion message successfully sent to the queue");
    }

    @Override
    public ResponseEntity<String> postSupplier(SupplierMessage supplierMessage) {
        LOGGER.info("Received request to create supplier: {}", supplierMessage);
        supplierService.sendSupplierCreateMessage(supplierMessage);
        LOGGER.info("Supplier created successfully");
        return ResponseEntity.ok("Supplier creation message successfully sent to the queue");
    }
}
