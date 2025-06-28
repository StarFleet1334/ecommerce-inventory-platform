package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.EmployeeMP;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
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

    public void handleNewEmployee(EmployeeMP employee) {

    }

    @Transactional
    public void handleDeleteEmployee(String id) {
        LOGGER.info("Processing employee deletion for ID: {}", id);

    }
}
