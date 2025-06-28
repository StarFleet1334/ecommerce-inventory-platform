package com.example.orderprocessingservice.repository.supplier;

import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
