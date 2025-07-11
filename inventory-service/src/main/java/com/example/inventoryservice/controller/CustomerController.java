package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.messages.CustomerMessage;
import com.example.inventoryservice.entity.messages.CustomerOrderMessage;
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
        return ResponseEntity.ok("Customer deletion message successfully sent to the queue");
    }

    @Override
    public ResponseEntity<String> postCustomer(CustomerMessage customerMessage, boolean initialLoad) {
        LOGGER.info("Received request to create customer: {}", customerMessage);
        if (initialLoad) {
            customerService.sendCustomerInitialCreateMessage(customerMessage);
            LOGGER.info("Initial customer creation message successfully sent to the queue");
        } else {
            customerService.sendCustomerCreateMessage(customerMessage);
            LOGGER.info("Customer creation message successfully sent to the queue");
        }
        return ResponseEntity.ok("Customer creation message successfully sent to the queue");
    }

    @Override
    public ResponseEntity<String> postCustomerOrder(CustomerOrderMessage customerOrderMessage) {
        LOGGER.info("Received request to create customer order: {}", customerOrderMessage);
        customerService.sentCustomerOrderMessage(customerOrderMessage);
        LOGGER.info("Customer order created successfully");
        return ResponseEntity.ok("Customer order creation message successfully sent to the queue");
    }
}
