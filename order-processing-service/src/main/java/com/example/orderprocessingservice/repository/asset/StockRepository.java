package com.example.orderprocessingservice.repository.asset;

import com.example.orderprocessingservice.dto.model.asset.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}
