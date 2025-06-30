package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.WareHouseMP;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.mapper.warehouse.WarehouseMapper;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.validator.WareHouseValidator;
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
    private final WareHouseValidator wareHouseValidator;
    private final WarehouseMapper warehouseMapper;

    public void handleNewWareHouse(WareHouseMP wareHouse) {
        LOGGER.info("Processing new warehouse: {}", wareHouse);

        wareHouseValidator.validate(wareHouse);

        WareHouse newWareHouse = warehouseMapper.toEntity(wareHouse);

        try {
            wareHouseRepository.save(newWareHouse);
            LOGGER.info("Successfully saved new wareHouse with name: {}", wareHouse.getWare_house_name());
        } catch (Exception e) {
            LOGGER.error("Failed to save wareHouse: {}", e.getMessage());
            throw new RuntimeException("Failed to save wareHouse", e);
        }
    }

    @Transactional
    public void handleDeleteWareHouse(String id) {
        LOGGER.info("Processing warehouse deletion for ID: {}", id);
        try {
            int wareHouseId = Integer.parseInt(id);
            if (!wareHouseRepository.existsById(wareHouseId)) {
                throw new IllegalArgumentException("WareHouse with ID " + id + " does not exist");
            }
            wareHouseRepository.deleteById(wareHouseId);
            LOGGER.info("Successfully deleted wareHouse with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid wareHouse ID format: " + id);
        }
    }

}
