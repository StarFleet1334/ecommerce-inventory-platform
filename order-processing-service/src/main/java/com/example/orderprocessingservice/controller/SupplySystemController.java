package com.example.orderprocessingservice.controller;

import com.example.orderprocessingservice.service.SupplyOrderService;
import com.example.orderprocessingservice.skeleton.SupplySystemControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SupplySystemController implements SupplySystemControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplySystemController.class);
    private final SupplyOrderService supplyOrderService;

    @Override
    public ResponseEntity<String> speedUpEmployeesSupply(int supplyId) {
        LOGGER.info("Received request to speed up employees supply with ID: {}", supplyId);
        supplyOrderService.speedUpEmployeeSupply(supplyId);
        LOGGER.info("Finished request to speed up employees supply with ID: {}", supplyId);
        return ResponseEntity.ok("Successfully speed up employees supply with ID: " + supplyId);
    }
}
