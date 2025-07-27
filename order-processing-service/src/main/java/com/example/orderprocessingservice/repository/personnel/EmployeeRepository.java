package com.example.orderprocessingservice.repository.personnel;

import com.example.orderprocessingservice.dto.model.personnel.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE Employee e SET e.wareHouse = null WHERE e.wareHouse.wareHouseId = :wareHouseId")
    void updateEmployeeWarehouseToNull(@Param("wareHouseId") int wareHouseId);

    @Query("SELECT e FROM Employee e WHERE e.employee_id = :employeeId")
    Optional<Employee> findByEmployeeId(@Param("employeeId") int employeeId);

    @Query("SELECT e FROM Employee e ORDER BY e.lastName,e.firstName")
    List<Employee> findAllEmployees();

}
