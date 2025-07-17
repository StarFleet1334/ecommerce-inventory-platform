package com.example.orderprocessingservice.exception.asset;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class StockException extends BaseException {
    public static final String STOCK_NOT_FOUND = "STOCK_001";
    public static final String STOCK_CAPACITY_EXCEEDED = "STOCK_003";

    private StockException(String message, HttpStatus status, String code) {
        super(status, code, message);
    }

    public static StockException notFound(String stockId) {
        return new StockException(
                String.format("Stock with ID %s not found", stockId),
                HttpStatus.NOT_FOUND,
                STOCK_NOT_FOUND
        );
    }

    public static StockException capacityExceeded(int warehouseId, int requested, int capacity) {
        return new StockException(
                String.format("Warehouse %s capacity exceeded. Requested: %d, Capacity: %d",
                        warehouseId, requested, capacity),
                HttpStatus.BAD_REQUEST,
                STOCK_CAPACITY_EXCEEDED
        );
    }
}
