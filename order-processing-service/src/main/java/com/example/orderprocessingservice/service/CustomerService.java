package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.CustomerMP;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final CustomerValidator customerValidator;

    @Transactional
    public void handleNewCustomer(CustomerMP customer) {
        LOGGER.info("Processing new customer: {}", customer);

        customerValidator.validate(customer);

        Customer newCustomer = mapToCustomer(customer);

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

    private Customer mapToCustomer(CustomerMP customer) {
        return Customer.builder()
                .first_name(customer.getFirst_name())
                .last_name(customer.getLast_name())
                .email(customer.getEmail())
                .phone_number(customer.getPhone_number())
                .latitude(customer.getLatitude())
                .longitude(customer.getLongitude())
                .build();
    }
}
