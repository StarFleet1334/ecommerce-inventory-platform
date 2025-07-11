package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.messages.EmployeeMessage;
import com.example.inventoryservice.service.EmployeeService;
import com.example.inventoryservice.skeleton.EmployeeControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        LOGGER.info("Received request to delete employee with id: {}", id);
        employeeService.sendEmployeeDeleteMessage(id);
        LOGGER.info("Employee deleted successfully");
        return ResponseEntity.ok("Employee deletion message successfully sent to the queue");
    }

    @Override
    public ResponseEntity<String> postEmployee(EmployeeMessage employeeMessage, boolean initialLoad) {
        LOGGER.info("Received request to create employee: {}", employeeMessage);
        if (initialLoad) {
            employeeService.sendEmployeeInitialCreateMessage(employeeMessage);
            LOGGER.info("Initial employee creation message successfully sent to the queue");
        } else {
            employeeService.sendEmployeeCreateMessage(employeeMessage);
            LOGGER.info("Employee creation message successfully sent to the queue");
        }
        return ResponseEntity.ok("Employee creation message successfully sent to the queue");
    }
}
