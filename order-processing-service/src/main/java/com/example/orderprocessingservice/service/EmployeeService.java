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
import java.util.List;
import java.util.Optional;

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
        validateEmployeeRequest(employee);
        Employee savedEmployee = createAndSaveEmployee(employee);
        LOGGER.info("Successfully processed new employee with ID: {}", savedEmployee.getEmployee_id());
    }

    @Transactional
    public void handleDeleteEmployee(String id) {
        LOGGER.info("Processing employee deletion for ID: {}", id);
        int employeeId = parseEmployeeId(id);
        validateEmployeeExists(employeeId);
        executeEmployeeDeletion(employeeId);
        LOGGER.info("Successfully deleted employee with ID: {}", id);
    }

    public Employee getEmployeeById(int id) {
        LOGGER.info("Processing retrieval of employee with ID: {}", id);
        Optional<Employee> employee = employeeRepository.findByEmployeeId(id);
        if (employee.isEmpty()) {
            LOGGER.warn("Employee with ID {} not found", id);
            throw EmployeeException.notFound(id);
        }
        LOGGER.debug("Successfully retrieved employee with ID: {}", id);
        return employee.get();
    }

    public List<Employee> getAllEmployee() {
        LOGGER.info("Retrieving all employees");
        List<Employee> employees = employeeRepository.findAllEmployees();
        LOGGER.info("Successfully retrieved {} employees", employees.size());
        return employees;
    }

    // Helper Methods ---------------------------------------------------------------------------------------

    private void validateEmployeeRequest(EmployeeMP employeeRequest) {
        try {
            employeeValidator.validate(employeeRequest);
            LOGGER.debug("Employee validation successful");
        } catch (Exception e) {
            LOGGER.error("Employee validation failed: {}", e.getMessage());
            throw e;
        }
    }

    private Employee createAndSaveEmployee(EmployeeMP employeeRequest) {
        try {
            Employee newEmployee = employeeMapper.toEntity(employeeRequest);
            employeeRepository.save(newEmployee);
            LOGGER.info("Successfully saved new employee with email: {}", employeeRequest.getEmail());
            return newEmployee;
        } catch (Exception e) {
            LOGGER.error("Failed to save employee: {}", e.getMessage());
            throw new RuntimeException("Failed to save employee", e);
        }
    }

    private int parseEmployeeId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid employee ID format: {}", id);
            throw new IllegalArgumentException("Invalid employee ID format: " + id);
        }
    }

    private void validateEmployeeExists(int employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            LOGGER.warn("Employee with ID {} not found for deletion", employeeId);
            throw EmployeeException.notFound(employeeId);
        }
    }

    private void executeEmployeeDeletion(int employeeId) {
        try {
            employeeRepository.deleteById(employeeId);
            LOGGER.debug("Successfully executed deletion for employee ID: {}", employeeId);
        } catch (Exception e) {
            LOGGER.error("Failed to delete employee with ID {}: {}", employeeId, e.getMessage());
            throw new RuntimeException("Failed to delete employee with ID: " + employeeId, e);
        }
    }

}
