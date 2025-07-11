package com.example.orderprocessingservice.config.database.impl;

import com.example.orderprocessingservice.config.database.loaders.EmployeeLoader;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeLoaderImpl extends EmployeeLoader {
    public EmployeeLoaderImpl(ObjectMapper objectMapper, EmployeeRepository repository, WareHouseRepository wareHouseRepository) {
        super(objectMapper, repository, wareHouseRepository);
    }
}
