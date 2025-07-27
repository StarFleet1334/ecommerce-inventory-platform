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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierService.class);
    private final SupplierRepository supplierRepository;
    private final SupplierValidator supplierValidator;
    private final SupplierMapper supplierMapper;

    public void handleNewSupplier(SupplierMP supplierMP) {
        LOGGER.info("Processing new supplier: {}", supplierMP);
        validateSupplier(supplierMP);
        Supplier supplier = convertToEntity(supplierMP);
        saveSupplier(supplier);
        LOGGER.info("Successfully processed new supplier with email: {}", supplierMP.getEmail());
    }

    @Transactional
    public void handleDeleteSupplier(String id) {
        LOGGER.info("Processing supplier deletion for ID: {}", id);
        int supplierId = parseAndValidateId(id);
        validateSupplierExists(supplierId);
        deleteSupplierById(supplierId);
        LOGGER.info("Successfully deleted supplier with ID: {}", id);
    }

    public Supplier getSupplierById(int id) {
        LOGGER.info("Fetching supplier with ID: {}", id);
        return findSupplierById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Supplier not found with ID: {}", id);
                    return SupplierException.notFound(id);
                });
    }

    public List<Supplier> getAllSupplier() {
        LOGGER.info("Fetching all suppliers");
        List<Supplier> suppliers = supplierRepository.findAll();
        LOGGER.debug("Suppliers found successfully");
        return suppliers;
    }


    // Helper Methods ---------------------------------------------------------------------------------------

    private void validateSupplier(SupplierMP supplier) {
        LOGGER.debug("Validating supplier: {}", supplier.getEmail());
        supplierValidator.validate(supplier);
    }

    private Supplier convertToEntity(SupplierMP supplierMP) {
        LOGGER.debug("Converting SupplierMP to entity");
        return supplierMapper.toEntity(supplierMP);
    }

    private void saveSupplier(Supplier supplier) {
        try {
            LOGGER.debug("Saving supplier to database");
            supplierRepository.save(supplier);
        } catch (Exception e) {
            LOGGER.error("Failed to save supplier: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save supplier", e);
        }
    }

    private int parseAndValidateId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid supplier ID format: {}", id);
            throw new IllegalArgumentException("Invalid supplier ID format: " + id, e);
        }
    }

    private boolean supplierExists(int id) {
        LOGGER.debug("Checking if supplier exists with ID: {}", id);
        return supplierRepository.existsById(id);
    }

    private void validateSupplierExists(int supplierId) {
        if (!supplierExists(supplierId)) {
            LOGGER.error("Supplier not found with ID: {}", supplierId);
            throw SupplierException.notFound(supplierId);
        }
    }

    private void deleteSupplierById(int supplierId) {
        try {
            LOGGER.debug("Deleting supplier with ID: {}", supplierId);
            supplierRepository.deleteById(supplierId);
        } catch (Exception e) {
            LOGGER.error("Failed to delete supplier with ID {}: {}", supplierId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete supplier", e);
        }
    }

    private Optional<Supplier> findSupplierById(int id) {
        LOGGER.debug("Searching for supplier with ID: {}", id);
        return supplierRepository.findById(id);
    }
}
