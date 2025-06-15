package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.CustomerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, Integer> {
}
