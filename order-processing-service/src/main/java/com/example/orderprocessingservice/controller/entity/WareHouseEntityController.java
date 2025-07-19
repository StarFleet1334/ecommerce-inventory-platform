package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.exception.personnel.WareHouseException;
import com.example.orderprocessingservice.service.WareHouseService;
import com.example.orderprocessingservice.skeleton.entity.WareHouseEntityControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WareHouseEntityController implements WareHouseEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(WareHouseEntityController.class);
    private final WareHouseService wareHouseService;

    @Override
    public ResponseEntity<Object> getWareHouseById(String id) {
        try {
            LOGGER.info("Received request to retrieve warehouse with ID: {}", id);
            WareHouse wareHouse = wareHouseService.getWareHouseById(id);
            LOGGER.info("Successfully retrieved warehouse with ID: {}", id);
            return ResponseEntity.ok(wareHouse);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid warehouse ID format: " + id);
        } catch (WareHouseException e) {
            LOGGER.warn("WareHouse with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "WareHouse with ID " + id + " not found", "message", "WareHouse not found. Please check the ID and try again. If the problem persists, contact your system administrator."));
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve warehouse with ID: {}", id, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<List<WareHouse>> getAllWareHouse() {
        try {
            List<WareHouse> wareHouses = wareHouseService.getAllWareHouse();
            LOGGER.info("Successfully retrieved all warehouses");
            return ResponseEntity.ok(wareHouses);
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all warehouses", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}
