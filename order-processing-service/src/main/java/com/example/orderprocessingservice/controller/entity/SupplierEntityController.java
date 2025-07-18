package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.skeleton.entity.SupplierEntityControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SupplierEntityController implements SupplierEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierEntityController.class);

    @Override
    public ResponseEntity<Supplier> getSupplierById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Supplier>> getAllSupplier() {
        return null;
    }
}
