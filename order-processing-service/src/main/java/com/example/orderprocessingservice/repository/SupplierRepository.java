package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
