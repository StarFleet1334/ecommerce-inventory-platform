package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.skeleton.entity.WareHouseEntityControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WareHouseEntityController implements WareHouseEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(WareHouseEntityController.class);


    @Override
    public ResponseEntity<WareHouse> getWareHouseById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<List<WareHouse>> getAllWareHouse() {
        return null;
    }
}
