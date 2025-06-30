package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.CustomerMP;
import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.mapper.customer.CustomerMapper;
import com.example.orderprocessingservice.mapper.customer.CustomerOrderMapper;
import com.example.orderprocessingservice.repository.customer.CustomerOrderRepository;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.repository.transaction.CustomerTransactionRepository;
import com.example.orderprocessingservice.validator.CustomerOrderValidator;
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
    private final CustomerTransactionRepository customerTransactionRepository;
    private final CustomerValidator customerValidator;
    private final CustomerOrderValidator customerOrderValidator;
    private final CustomerMapper customerMapper;
    private final CustomerOrderMapper customerOrderMapper;
    private final CustomerOrderRepository customerOrderRepository;

    @Transactional
    public void handleNewCustomer(CustomerMP customer) {
        LOGGER.info("Processing new customer: {}", customer);

        customerValidator.validate(customer);

        Customer newCustomer = customerMapper.toEntity(customer);

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
                throw CustomerException.notFound(customerId);
            }
            customerRepository.deleteById(customerId);
            LOGGER.info("Successfully deleted customer with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid customer ID format: " + id);
        }
    }

    @Transactional
    public void handleCustomerNewOrder(CustomerOrderMP customerOrder) {
        LOGGER.info("Processing new customer order: {}", customerOrder);

        customerOrderValidator.validate(customerOrder);

        CustomerOrder newCustomerOrder = customerOrderMapper.toEntity(customerOrder);

        try {
            customerOrderRepository.save(newCustomerOrder);
            // ... here should follow later customer transaction table saving and logic
            // of time computation based on location of customer and wareHouse (For Now, Before Drivers are implemented!)
            LOGGER.info("Successfully saved new customer order: {}",customerOrder);
        } catch (Exception e) {
            LOGGER.error("Failed to save customer order: {}", e.getMessage());
            throw new RuntimeException("Failed to save customer order", e);
        }


    }

}
