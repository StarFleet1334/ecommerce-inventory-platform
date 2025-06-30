package com.example.orderprocessingservice.exception.customer;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class CustomerException extends BaseException {
    public static final String CUSTOMER_NOT_FOUND = "CUSTOMER_001";

    private CustomerException(String message, HttpStatus status, String code) {
        super(status, code, message);
    }

    public static CustomerException notFound(int customerId) {
        return new CustomerException(
                String.format("Customer with ID %s not found", customerId),
                HttpStatus.NOT_FOUND,
                CUSTOMER_NOT_FOUND
        );
    }
}
