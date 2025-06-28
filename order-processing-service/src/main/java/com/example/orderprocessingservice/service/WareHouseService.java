package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.WareHouseMP;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WareHouseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WareHouseService.class);
    private final WareHouseRepository wareHouseRepository;

    public void handleNewWareHouse(WareHouseMP wareHouse) {
        LOGGER.info("Processing new warehouse: {}", wareHouse);
        if (wareHouse.getWare_house_name() == null || wareHouse.getLatitude() == null || wareHouse.getLongitude() == null) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (wareHouse.getWare_house_name().length() > 20) {
            throw new IllegalArgumentException("Warehouse name must not exceed 20 characters");
        }

        if (wareHouse.getMin_stock_level() < 0) {
            throw new IllegalArgumentException("Minimum stock level must be greater than or equal to 0");
        }

        if (wareHouse.getMax_stock_level() < 0) {
            throw new IllegalArgumentException("Maximum stock level must be greater than or equal to 0");
        }

        if (wareHouse.getMin_stock_level() > wareHouse.getMax_stock_level()) {
            throw new IllegalArgumentException("Minimum stock level must be less than or equal to maximum stock level");
        }

        // Validating uniqueness
        if (wareHouseRepository.existsByWareHouseName(wareHouse.getWare_house_name())) {
            throw new IllegalArgumentException("Warehouse name already exists");
        }

        // Validating latitude and longitude

        if (wareHouse.getLatitude().compareTo(new BigDecimal("-90")) < 0 ||
                wareHouse.getLatitude().compareTo(new BigDecimal("90")) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (wareHouse.getLongitude().compareTo(new BigDecimal("-180")) < 0 ||
                wareHouse.getLongitude().compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }

        WareHouse newWareHouse = WareHouse.builder()
                .ware_house_name(wareHouse.getWare_house_name())
                .ware_house_capacity(0)
                .refrigerated(wareHouse.is_refrigerated())
                .min_stock_level(wareHouse.getMin_stock_level())
                .max_stock_level(wareHouse.getMax_stock_level())
                .latitude(wareHouse.getLatitude())
                .longitude(wareHouse.getLongitude())
                .build();

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
