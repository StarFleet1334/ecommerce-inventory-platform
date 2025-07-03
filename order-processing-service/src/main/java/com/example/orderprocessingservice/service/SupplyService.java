package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.SupplyMP;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.mapper.supplier.SupplierMapper;
import com.example.orderprocessingservice.repository.asset.SupplyRepository;
import com.example.orderprocessingservice.validator.SupplyValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplyService.class);
    private final SupplyRepository supplyRepository;
    private final SupplyValidator supplyValidator;
    private final SupplierMapper supplierMapper;

    @Transactional
    public void handleNewSupply(SupplyMP supply) {
        LOGGER.info("Processing new supply: {}", supply);

    }

    @Transactional
    public void handleDeleteSupply(String id) {
        LOGGER.info("Processing supply deletion for ID: {}", id);
        try {
            int supplyId = Integer.parseInt(id);
            if (!supplyRepository.existsById(supplyId)) {
                throw CustomerException.notFound(supplyId);
            }
            supplyRepository.deleteById(supplyId);
            LOGGER.info("Successfully deleted supply with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid supply ID format: " + id);
        }
    }
}
