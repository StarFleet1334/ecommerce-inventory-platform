package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.SupplierMP;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
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

    public void handleNewSupplier(SupplierMP supplierMP) {

    }

    @Transactional
    public void handleDeleteSupplier(String id) {
        LOGGER.info("Processing supplier deletion for ID: {}", id);

    }
}
