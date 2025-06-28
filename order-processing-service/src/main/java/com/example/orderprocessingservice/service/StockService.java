package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.StockMP;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    private final StockRepository stockRepository;

    public void handleNewStock(StockMP stock) {
        LOGGER.info("Processing new stock: {}", stock);

    }

    @Transactional
    public void handleDeleteStock(String id) {
        LOGGER.info("Processing stock deletion for ID: {}", id);
        // Deletion of Stock is not supported at the moment
    }
}
