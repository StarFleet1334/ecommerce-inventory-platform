package com.example.orderprocessingservice.exception.asset;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ProductException extends BaseException {
    public static final String PRODUCT_NOT_FOUND = "PRODUCT_001";

    private ProductException(String message, HttpStatus status, String code) {
        super(status, code, message);
    }

    public static ProductException notFound(String productId) {
        return new ProductException(
                String.format("Product with ID %s not found", productId),
                HttpStatus.NOT_FOUND,
                PRODUCT_NOT_FOUND
        );
    }

}
