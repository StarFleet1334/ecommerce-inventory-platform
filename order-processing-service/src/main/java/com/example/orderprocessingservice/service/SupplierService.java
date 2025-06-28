package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.SupplierMP;
import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierService.class);
    private final SupplierRepository supplierRepository;

    public void handleNewSupplier(SupplierMP supplier) {
        LOGGER.info("Processing new supplier: {}", supplier);

        if (supplier.getFirst_name() == null || supplier.getLast_name() == null ||
                supplier.getEmail() == null || supplier.getPhone_number() == null ||
                supplier.getLatitude() == null || supplier.getLongitude() == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        if (supplier.getFirst_name().length() > 10) {
            throw new IllegalArgumentException("First name must not exceed 10 characters");
        }
        if (supplier.getLast_name().length() > 10) {
            throw new IllegalArgumentException("Last name must not exceed 10 characters");
        }
        if (supplier.getEmail().length() > 50) {
            throw new IllegalArgumentException("Email must not exceed 50 characters");
        }
        if (supplier.getPhone_number().length() > 15) {
            throw new IllegalArgumentException("Phone number must not exceed 15 characters");
        }

        // Validating uniqueness
        if (supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (supplierRepository.existsByPhoneNumber(supplier.getPhone_number())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        if (supplier.getLatitude().compareTo(new BigDecimal("-90")) < 0 ||
                supplier.getLatitude().compareTo(new BigDecimal("90")) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (supplier.getLongitude().compareTo(new BigDecimal("-180")) < 0 ||
                supplier.getLongitude().compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }

        Supplier newSupplier = Supplier.builder()
                .first_name(supplier.getFirst_name())
                .last_name(supplier.getLast_name())
                .email(supplier.getEmail())
                .phone_number(supplier.getPhone_number())
                .latitude(supplier.getLatitude())
                .longitude(supplier.getLongitude())
                .build();

        try {
            supplierRepository.save(newSupplier);
            LOGGER.info("Successfully saved new supplier with email: {}", supplier.getEmail());
        } catch (Exception e) {
            LOGGER.error("Failed to save supplier: {}", e.getMessage());
            throw new RuntimeException("Failed to save supplier", e);
        }
    }

    @Transactional
    public void handleDeleteSupplier(String id) {
        LOGGER.info("Processing supplier deletion for ID: {}", id);
        try {
            int supplierId = Integer.parseInt(id);
            if (!supplierRepository.existsById(supplierId)) {
                throw new IllegalArgumentException("Supplier with ID " + id + " does not exist");
            }
            supplierRepository.deleteById(supplierId);
            LOGGER.info("Successfully deleted supplier with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid supplier ID format: " + id);
        }
    }
}
