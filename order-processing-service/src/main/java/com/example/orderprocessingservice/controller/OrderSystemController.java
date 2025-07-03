package com.example.orderprocessingservice.controller;

import com.example.orderprocessingservice.exception.customer.CustomerOrderException;
import com.example.orderprocessingservice.service.CustomerOrderService;
import com.example.orderprocessingservice.skeleton.OrderSystemControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderSystemController implements OrderSystemControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSystemController.class);
    private final CustomerOrderService customerOrderService;

    @Override
    public ResponseEntity<String> speedUpCustomersOrder(int orderId) {
        try {
            LOGGER.info("Received request to speed up customer order with ID: {}", orderId);
            customerOrderService.speedUpCustomerOrder(orderId);
            return ResponseEntity.ok("Successfully speed up customer order with ID: " + orderId);
        } catch (CustomerOrderException customerOrderException) {
            LOGGER.error("Failed to speed up customer order with ID: {}", orderId, customerOrderException);
            return ResponseEntity.badRequest().body("Failed to speed up customer order with ID: " + orderId);
        } catch (Exception e) {
            LOGGER.error("Failed to speed up customer order with ID: {}", orderId, e);
            return ResponseEntity.internalServerError().body("Failed to speed up customer order with ID: " + orderId);
        } finally {
            LOGGER.info("Finished request to speed up customer order with ID: {}", orderId);
        }
    }
}
