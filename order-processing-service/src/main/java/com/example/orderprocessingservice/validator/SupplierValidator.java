package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierValidator {
    private final SupplierRepository supplierRepository;
}
