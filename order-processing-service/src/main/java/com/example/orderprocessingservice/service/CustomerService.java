package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.CustomerMP;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public void handleNewCustomer(CustomerMP customer) {
        LOGGER.info("Processing new customer: {}", customer);
        if (customer.getFirst_name() == null || customer.getLast_name() == null ||
                customer.getEmail() == null || customer.getPhone_number() == null ||
                customer.getLatitude() == null || customer.getLongitude() == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        if (customer.getFirst_name().length() > 10) {
            throw new IllegalArgumentException("First name must not exceed 10 characters");
        }
        if (customer.getLast_name().length() > 10) {
            throw new IllegalArgumentException("Last name must not exceed 10 characters");
        }
        if (customer.getEmail().length() > 50) {
            throw new IllegalArgumentException("Email must not exceed 50 characters");
        }
        if (customer.getPhone_number().length() > 15) {
            throw new IllegalArgumentException("Phone number must not exceed 15 characters");
        }

        // Validating uniqueness
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (customerRepository.existsByPhoneNumber(customer.getPhone_number())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        // Validating latitude and longitude
        if (customer.getLatitude().compareTo(new BigDecimal("-90")) < 0 ||
                customer.getLatitude().compareTo(new BigDecimal("90")) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (customer.getLongitude().compareTo(new BigDecimal("-180")) < 0 ||
                customer.getLongitude().compareTo(new BigDecimal("180")) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }

        Customer newCustomer = Customer.builder()
                .first_name(customer.getFirst_name())
                .last_name(customer.getLast_name())
                .email(customer.getEmail())
                .phone_number(customer.getPhone_number())
                .latitude(customer.getLatitude())
                .longitude(customer.getLongitude())
                .build();

        try {
            customerRepository.save(newCustomer);
            LOGGER.info("Successfully saved new customer with email: {}", customer.getEmail());
        } catch (Exception e) {
            LOGGER.error("Failed to save customer: {}", e.getMessage());
            throw new RuntimeException("Failed to save customer", e);
        }
    }

    @Transactional
    public void handleDeleteCustomer(String id) {
        LOGGER.info("Processing customer deletion for ID: {}", id);
        try {
            int customerId = Integer.parseInt(id);
            if (!customerRepository.existsById(customerId)) {
                throw new IllegalArgumentException("Customer with ID " + id + " does not exist");
            }
            customerRepository.deleteById(customerId);
            LOGGER.info("Successfully deleted customer with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid customer ID format: " + id);
        }
    }
}
