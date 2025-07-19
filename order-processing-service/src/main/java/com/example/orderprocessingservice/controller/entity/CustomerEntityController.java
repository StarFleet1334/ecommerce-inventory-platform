package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.service.CustomerService;
import com.example.orderprocessingservice.skeleton.entity.CustomerEntityControllerInterface;
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
public class CustomerEntityController implements CustomerEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerEntityController.class);
    private final CustomerService customerService;

    @Override
    public ResponseEntity<Object> getCustomerById(String id) {
        try {
            int customerId = Integer.parseInt(id);
            Customer customer = customerService.getCustomerById(customerId);
            return ResponseEntity.ok(customer);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid customer ID format: " + id);
        } catch (CustomerException e) {
            LOGGER.warn("Customer with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Customer with ID " + id + " not found", "message", "Customer not found. Please check the ID and try again. If the problem persists, contact your system administrator."));
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve customer with ID: {}", id, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all customers", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}
