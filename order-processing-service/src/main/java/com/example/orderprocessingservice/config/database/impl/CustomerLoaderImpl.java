package com.example.orderprocessingservice.config.database.impl;

import com.example.orderprocessingservice.config.database.loaders.CustomerLoader;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerLoaderImpl extends CustomerLoader {
    public CustomerLoaderImpl(ObjectMapper objectMapper, CustomerRepository repository) {
        super(objectMapper, repository);
    }
}
