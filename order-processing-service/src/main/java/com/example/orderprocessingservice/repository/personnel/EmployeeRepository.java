package com.example.orderprocessingservice.repository.personnel;

import com.example.orderprocessingservice.dto.model.personnel.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE Employee e SET e.wareHouse = null WHERE e.wareHouse.wareHouseId = :wareHouseId")
    void updateEmployeeWarehouseToNull(@Param("wareHouseId") int wareHouseId);

}
