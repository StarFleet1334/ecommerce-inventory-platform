package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.exception.supplier.SupplierException;
import com.example.orderprocessingservice.service.SupplierService;
import com.example.orderprocessingservice.skeleton.entity.SupplierEntityControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SupplierEntityController implements SupplierEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierEntityController.class);
    private final SupplierService supplierService;

    @Override
    public ResponseEntity<Object> getSupplierById(String id) {
        try {
            int supplierId = Integer.parseInt(id);
            Supplier supplier = supplierService.getSupplierById(supplierId);
            return ResponseEntity.ok(supplier);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid supplier ID format: " + id);
        } catch (SupplierException e) {
            LOGGER.warn("Supplier with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Supplier with ID " + id + " not found", "message", "Supplier not found. Please check the ID and try again. If the problem persists, contact your system administrator."));
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
