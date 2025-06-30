package com.example.orderprocessingservice.exception.personnel;

import com.example.orderprocessingservice.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class EmployeeException extends BaseException {
    public static final String EMPLOYEE_NOT_FOUND = "EMPLOYEE_001";

    private EmployeeException(String message, HttpStatus status, String code) {
        super(status, code, message);
    }

    public static EmployeeException notFound(int employeeId) {
        return new EmployeeException(
                String.format("Employee with ID %s not found", employeeId),
                HttpStatus.NOT_FOUND,
                EMPLOYEE_NOT_FOUND
        );
    }
}
