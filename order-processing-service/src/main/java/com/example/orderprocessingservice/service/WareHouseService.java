package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.WareHouseMP;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WareHouseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WareHouseService.class);
    private final WareHouseRepository wareHouseRepository;

    public void handleNewWareHouse(WareHouseMP wareHouse) {

    }

    @Transactional
    public void handleDeleteWareHouse(String id) {
        LOGGER.info("Processing warehouse deletion for ID: {}", id);

    }
}
