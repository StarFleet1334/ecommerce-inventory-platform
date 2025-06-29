package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.mapped.EmployeeMP;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
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
    private final WareHouseRepository wareHouseRepository;
    private final EmployeeValidator employeeValidator;

    @Transactional
    public void handleNewEmployee(EmployeeMP employee) {
        LOGGER.info("Processing new employee: {}", employee);

        employeeValidator.validate(employee);

        WareHouse wareHouse = wareHouseRepository.findById(employee.getWare_house_id())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse with ID " + employee.getWare_house_id() + " does not exist"));

        Employee newEmployee = mapToEmployee(employee, wareHouse);

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
                throw new IllegalArgumentException("Employee with ID " + id + " does not exist");
            }
            employeeRepository.deleteById(employeeId);
            LOGGER.info("Successfully deleted employee with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid employee ID format: " + id);
        }
    }

    private Employee mapToEmployee(EmployeeMP employeeMP, WareHouse wareHouse) {
        return Employee.builder()
                .first_name(employeeMP.getFirst_name())
                .last_name(employeeMP.getLast_name())
                .email(employeeMP.getEmail())
                .phone_number(employeeMP.getPhone_number())
                .wareHouse(wareHouse)
                .build();
    }

}
