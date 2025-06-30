package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.EmployeeMP;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.exception.personnel.EmployeeException;
import com.example.orderprocessingservice.mapper.employee.EmployeeMapper;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.validator.EmployeeValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeValidator employeeValidator;
    private final EmployeeMapper employeeMapper;

    @Transactional
    public void handleNewEmployee(EmployeeMP employee) {
        LOGGER.info("Processing new employee: {}", employee);

        employeeValidator.validate(employee);

        Employee newEmployee = employeeMapper.toEntity(employee);

        try {
            employeeRepository.save(newEmployee);
            LOGGER.info("Successfully saved new employee with email: {}", employee.getEmail());
        } catch (Exception e) {
            LOGGER.error("Failed to save employee: {}", e.getMessage());
            throw new RuntimeException("Failed to save employee", e);
        }
    }

    @Transactional
    public void handleDeleteEmployee(String id) {
        LOGGER.info("Processing employee deletion for ID: {}", id);
        try {
            int employeeId = Integer.parseInt(id);
            if (!employeeRepository.existsById(employeeId)) {
                throw EmployeeException.notFound(employeeId);
            }
            employeeRepository.deleteById(employeeId);
            LOGGER.info("Successfully deleted employee with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid employee ID format: " + id);
        }
    }


}
