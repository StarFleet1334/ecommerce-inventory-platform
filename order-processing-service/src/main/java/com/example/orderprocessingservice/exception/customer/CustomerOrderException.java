package com.example.orderprocessingservice.exception.customer;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class CustomerOrderException extends BaseException {
    public static final String CUSTOMER_NOT_FOUND = "CUSTOMER_ORDER_001";

    private CustomerOrderException(String message, HttpStatus status, String code) {
        super(status, code, message);
    }

    public static CustomerOrderException notFound(int customerId) {
        return new CustomerOrderException(
                String.format("Customer order with ID %s not found", customerId),
                HttpStatus.NOT_FOUND,
                CUSTOMER_NOT_FOUND
        );
    }
}
