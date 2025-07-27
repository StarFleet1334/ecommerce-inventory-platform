package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.WareHouseMP;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.exception.personnel.WareHouseException;
import com.example.orderprocessingservice.mapper.warehouse.WarehouseMapper;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.validator.WareHouseValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WareHouseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WareHouseService.class);
    private final WareHouseRepository wareHouseRepository;
    private final WareHouseValidator wareHouseValidator;
    private final WarehouseMapper warehouseMapper;
    private final StockRepository stockRepository;
    private final EmployeeRepository employeeRepository;

    public void handleNewWareHouse(WareHouseMP wareHouse) {
        LOGGER.info("Processing new warehouse: {}", wareHouse);
        validateWareHouseRequest(wareHouse);
        WareHouse savedWareHouse = createAndSaveWareHouse(wareHouse);
        LOGGER.info("Successfully processed new warehouse with ID: {}", savedWareHouse.getWareHouseId());
    }

    @Transactional
    public void handleDeleteWareHouse(String id) {
        LOGGER.info("Processing warehouse deletion for ID: {}", id);
        try {
            int wareHouseId = parseWareHouseId(id);
            if (!wareHouseRepository.existsById(wareHouseId)) {
                throw WareHouseException.notFound(wareHouseId);
            }
            employeeRepository.updateEmployeeWarehouseToNull(wareHouseId);
            LOGGER.info("Successfully updated all employees warehouse to null for wareHouse with ID: {}", id);

            stockRepository.deleteAllByWareHouseId(wareHouseId);
            LOGGER.info("Successfully deleted all stocks for wareHouse with ID: {}", id);

            wareHouseRepository.deleteById(wareHouseId);
            LOGGER.info("Successfully deleted wareHouse with ID: {}", id);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid wareHouse ID format: " + id);
        }
    }

    public WareHouse getWareHouseById(String id) {
        LOGGER.info("Fetching warehouse with ID: {}", id);
        int wareHouseId = parseWareHouseId(id);
        return getWareHouseById(wareHouseId);
    }

    public List<WareHouse> getAllWareHouse() {
        LOGGER.info("Fetching all warehouses");
        List<WareHouse> wareHouses = wareHouseRepository.findAll();
        LOGGER.info("Successfully retrieved {} warehouses", wareHouses.size());
        return wareHouses;
    }

    // Helper Methods ---------------------------------------------------------------------------------------

    private WareHouse getWareHouseById(int wareHouseId) {
        LOGGER.debug("Fetching warehouse with ID: {}", wareHouseId);

        Optional<WareHouse> wareHouse = wareHouseRepository.findById(wareHouseId);
        if (wareHouse.isEmpty()) {
            LOGGER.error("WareHouse not found with ID: {}", wareHouseId);
            throw WareHouseException.notFound(wareHouseId);
        }

        LOGGER.debug("WareHouse found successfully with ID: {}", wareHouseId);
        return wareHouse.get();
    }

    private void validateWareHouseRequest(WareHouseMP wareHouseRequest) {
        try {
            wareHouseValidator.validate(wareHouseRequest);
            LOGGER.debug("Warehouse validation successful for: {}", wareHouseRequest.getWare_house_name());
        } catch (Exception e) {
            LOGGER.error("Warehouse validation failed: {}", e.getMessage());
            throw e;
        }
    }

    private WareHouse createAndSaveWareHouse(WareHouseMP wareHouseRequest) {
        try {
            WareHouse newWareHouse = warehouseMapper.toEntity(wareHouseRequest);
            wareHouseRepository.save(newWareHouse);
            LOGGER.info("Successfully saved new warehouse with name: {}", wareHouseRequest.getWare_house_name());
            return newWareHouse;
        } catch (Exception e) {
            LOGGER.error("Failed to save warehouse: {}", e.getMessage());
            throw new RuntimeException("Failed to save warehouse", e);
        }
    }

    private int parseWareHouseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid warehouse ID format: {}", id);
            throw new IllegalArgumentException("Invalid warehouse ID format: " + id);
        }
    }
}
