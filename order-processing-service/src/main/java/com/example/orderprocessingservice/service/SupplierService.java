package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.SupplierMP;
import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.exception.supplier.SupplierException;
import com.example.orderprocessingservice.mapper.supplier.SupplierMapper;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import com.example.orderprocessingservice.validator.SupplierValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierService.class);
    private final SupplierRepository supplierRepository;
    private final SupplierValidator supplierValidator;
    private final SupplierMapper supplierMapper;

    public void handleNewSupplier(SupplierMP supplier) {
        LOGGER.info("Processing new supplier: {}", supplier);

        supplierValidator.validate(supplier);

        Supplier newSupplier = supplierMapper.toEntity(supplier);

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
                throw SupplierException.notFound(supplierId);
            }
            supplierRepository.deleteById(supplierId);
            LOGGER.info("Successfully deleted supplier with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid supplier ID format: " + id);
        }
    }

}
