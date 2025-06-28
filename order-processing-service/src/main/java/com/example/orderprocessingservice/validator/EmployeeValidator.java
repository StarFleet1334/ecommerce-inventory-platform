package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.dto.mapped.EmployeeMP;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.utils.constants.EmployeeConstants;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {
    private final EmployeeRepository employeeRepository;

    public void validateEmployee(EmployeeMP employee) {
        validateRequiredFields(employee);
        validateStringLengths(employee);
        validateUniqueness(employee);
    }

    private void validateRequiredFields(EmployeeMP employee) {
        List<String> missingFields = new ArrayList<>();

        if (employee.getFirst_name() == null) missingFields.add("first_name");
        if (employee.getLast_name() == null) missingFields.add("last_name");
        if (employee.getEmail() == null) missingFields.add("email");
        if (employee.getPhone_number() == null) missingFields.add("phone_number");

        if (!missingFields.isEmpty()) {
            throw new ValidationException("Missing required fields: " + String.join(", ", missingFields));
        }
    }

    private void validateStringLengths(EmployeeMP employee) {
        if (employee.getFirst_name().length() > EmployeeConstants.MAX_FIRST_NAME_LENGTH) {
            throw new ValidationException(
                    String.format("First name must not exceed %d characters",
                            EmployeeConstants.MAX_FIRST_NAME_LENGTH)
            );
        }
        if (employee.getLast_name().length() > EmployeeConstants.MAX_LAST_NAME_LENGTH) {
            throw new ValidationException(
                    String.format("Last name must not exceed %d characters",
                            EmployeeConstants.MAX_LAST_NAME_LENGTH)
            );
        }
        if (employee.getEmail().length() > EmployeeConstants.MAX_EMAIL_LENGTH) {
            throw new ValidationException(
                    String.format("Email must not exceed %d characters",
                            EmployeeConstants.MAX_EMAIL_LENGTH)
            );
        }
        if (employee.getPhone_number().length() > EmployeeConstants.MAX_PHONE_NUMBER_LENGTH) {
            throw new ValidationException(
                    String.format("Phone number must not exceed %d characters",
                            EmployeeConstants.MAX_EMAIL_LENGTH)
            );
        }

    }

    private void validateUniqueness(EmployeeMP employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (employeeRepository.existsByPhoneNumber(employee.getPhone_number())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
    }
}
