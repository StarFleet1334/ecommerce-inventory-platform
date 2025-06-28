package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.CustomerMP;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
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

    public void handleNewCustomer(CustomerMP customer) {

    }

    @Transactional
    public void handleDeleteCustomer(String id) {
        LOGGER.info("Processing customer deletion for ID: {}", id);

    }
}
