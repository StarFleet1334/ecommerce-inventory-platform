package com.example.orderprocessingservice.controller.entity;

import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.exception.personnel.EmployeeException;
import com.example.orderprocessingservice.service.EmployeeService;
import com.example.orderprocessingservice.skeleton.entity.EmployeeEntityControllerInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmployeeEntityController implements EmployeeEntityControllerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeEntityController.class);
    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<Object> getEmployeeById(String id) {
        try {
            int employeeId = Integer.parseInt(id);
            Employee employee = employeeService.getEmployeeById(employeeId);
            return ResponseEntity.ok(employee);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid employee ID format: " + id);
        } catch (EmployeeException e) {
            LOGGER.warn("Employee with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Employee with ID " + id + " not found", "message", "Employee not found. Please check the ID and try again. If the problem persists, contact your system administrator."));
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve employee with ID: {}", id, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployee() {
        try {
            List<Employee> employees = employeeService.getAllEmployee();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all employees", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}
