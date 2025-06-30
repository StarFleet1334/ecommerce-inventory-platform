package com.example.orderprocessingservice.exception.supplier;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class SupplierException extends BaseException {
    public static final String SUPPLIER_NOT_FOUND = "SUPPLIER_001";

    private SupplierException(String message, HttpStatus status, String code) {
        super(status, code, message);
    }

    public static SupplierException notFound(int supplierId) {
        return new SupplierException(
                String.format("Supplier with ID %s not found", supplierId),
                HttpStatus.NOT_FOUND,
                SUPPLIER_NOT_FOUND
        );
    }
}
