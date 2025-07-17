package com.example.orderprocessingservice.config.database.loaders;

import com.example.orderprocessingservice.config.database.AbstractDataLoader;
import com.example.orderprocessingservice.dto.messages.WareHouseMessage;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.utils.constants.UrlConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class WareHouseLoader extends AbstractDataLoader<WareHouse, WareHouseMessage> {

    @Autowired
    public WareHouseLoader(ObjectMapper objectMapper,
                           WareHouseRepository repository) {
        super(objectMapper,
                "data/warehouses.json",
                UrlConstants.WAREHOUSE_POST_ENDPOINT,
                repository);
    }

    @Override
    protected TypeReference<List<WareHouse>> getTypeReference() {
        return new TypeReference<>() {
        };
    }

    @Override
    protected WareHouseMessage convertToMessage(WareHouse warehouse) {
        WareHouseMessage message = new WareHouseMessage();
        message.setWare_house_name(warehouse.getWareHouseName());
        message.setRefrigerated(warehouse.isRefrigerated());
        message.setMax_stock_level(warehouse.getMaxStockLevel());
        message.setMin_stock_level(warehouse.getMinStockLevel());
        message.setLatitude(warehouse.getLatitude());
        message.setLongitude(warehouse.getLongitude());
        return message;
    }

    @Override
    protected String getEntityIdentifier(WareHouse entity) {
        return entity.getWareHouseName();
    }

}
