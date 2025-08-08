package com.example.orderprocessingservice.config.database.impl;

import com.example.orderprocessingservice.config.database.loaders.StockLoader;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockLoaderImpl extends StockLoader {

    @Autowired
    public StockLoaderImpl(ObjectMapper objectMapper,
                           StockRepository repository,
                           WareHouseRepository wareHouseRepository,
                           ProductRepository productRepository) {
        super(objectMapper, repository, wareHouseRepository, productRepository);
    }

    @Override
    protected TypeReference<List<Stock>> getTypeReference() {
        return new TypeReference<>() {};
    }

}
