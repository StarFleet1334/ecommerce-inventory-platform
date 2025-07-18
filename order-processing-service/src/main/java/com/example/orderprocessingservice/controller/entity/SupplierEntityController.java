package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.service.SupplierService;
import com.example.orderprocessingservice.skeleton.entity.SupplierEntityControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SupplierEntityController implements SupplierEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierEntityController.class);
    private final SupplierService supplierService;

    @Override
    public ResponseEntity<Supplier> getSupplierById(String id) {
        try {
            int supplierId = Integer.parseInt(id);
            Supplier supplier = supplierService.getSupplierById(supplierId);
            return ResponseEntity.ok(supplier);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid supplier ID format: " + id);
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve supplier with ID: {}", id, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<List<Supplier>> getAllSupplier() {
        try {
            List<Supplier> suppliers = supplierService.getAllSupplier();
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all suppliers", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}
