package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.SupplyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyTransactionRepository extends JpaRepository<SupplyTransaction, Integer> {
}
