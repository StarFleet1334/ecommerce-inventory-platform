package com.example.orderprocessingservice.config.database.impl;

import com.example.orderprocessingservice.config.database.loaders.WareHouseLoader;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class WareHouseLoaderImpl extends WareHouseLoader {
    public WareHouseLoaderImpl(ObjectMapper objectMapper, WareHouseRepository repository) {
        super(objectMapper, repository);
    }
}
