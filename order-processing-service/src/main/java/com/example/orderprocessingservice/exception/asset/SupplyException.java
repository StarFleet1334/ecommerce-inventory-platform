package com.example.orderprocessingservice.exception.asset;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class SupplyException extends BaseException {
    public static final String STOCK_NOT_FOUND = "SUPPLY_001";

    private SupplyException(String message, HttpStatus status, String code) {
        super(status, code, message);
    }

    public static SupplyException notFound(String stockId) {
        return new SupplyException(
                String.format("Supply with ID %s not found", stockId),
                HttpStatus.NOT_FOUND,
                STOCK_NOT_FOUND
        );
    }

}
