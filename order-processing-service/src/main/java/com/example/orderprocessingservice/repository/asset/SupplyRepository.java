package com.example.orderprocessingservice.repository.asset;

import com.example.orderprocessingservice.dto.model.asset.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyRepository extends JpaRepository<Supply, Integer> {
}
