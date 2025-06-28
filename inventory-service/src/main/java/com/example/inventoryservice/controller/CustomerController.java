package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.messages.CustomerMessage;
import com.example.inventoryservice.service.CustomerService;
import com.example.inventoryservice.skeleton.CustomerControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    @Override
    public ResponseEntity<String> deleteCustomerById(String id) {
        LOGGER.info("Received request to delete customer with id: {}", id);
        customerService.sendCustomerDeleteMessage(id);
        LOGGER.info("Customer deleted successfully");
        return ResponseEntity.ok("Customer successfully deleted");
    }

    @Override
    public ResponseEntity<String> postCustomer(CustomerMessage customerMessage) {
        LOGGER.info("Received request to create customer: {}", customerMessage);
        customerService.sendCustomerCreateMessage(customerMessage);
        LOGGER.info("Customer created successfully");
        return ResponseEntity.ok("Customer successfully created");
    }
}
