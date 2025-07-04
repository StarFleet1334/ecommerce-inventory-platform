package com.example.orderprocessingservice.exception.personnel;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class WareHouseException extends BaseException {
    public static final String WAREHOUSE_NOT_FOUND = "WAREHOUSE_001";
    public static final String WAREHOUSE_CAPACITY_EXCEEDED = "WAREHOUSE_002";

    private WareHouseException(String message, HttpStatus status, String code) {
        super(status, code, message);
    }

    public static WareHouseException notFound(int warehouseId) {
        return new WareHouseException(
                String.format("Warehouse with ID %s not found", warehouseId),
                HttpStatus.NOT_FOUND,
                WAREHOUSE_NOT_FOUND
        );
    }

    public static WareHouseException capacityExceeded(int warehouseId, int requested, int capacity) {
        return new WareHouseException(
                String.format("Warehouse %s capacity exceeded. Requested: %d, Capacity: %d",
                        warehouseId, requested, capacity),
                HttpStatus.BAD_REQUEST,
                WAREHOUSE_CAPACITY_EXCEEDED
        );
    }
}
