package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
