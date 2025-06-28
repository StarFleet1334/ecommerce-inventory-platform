package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerValidator {
    private final CustomerRepository customerRepository;
}
