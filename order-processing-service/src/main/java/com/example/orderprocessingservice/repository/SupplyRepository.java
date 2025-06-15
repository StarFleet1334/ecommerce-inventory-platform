package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyRepository extends JpaRepository<Supply, Integer> {
}
