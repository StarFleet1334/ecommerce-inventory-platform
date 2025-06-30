package com.example.orderprocessingservice.exception.personnel;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class WareHouseException extends BaseException {
    public static final String WAREHOUSE_NOT_FOUND = "WAREHOUSE_001";

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
}
