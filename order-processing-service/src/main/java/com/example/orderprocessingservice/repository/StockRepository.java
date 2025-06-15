package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}
