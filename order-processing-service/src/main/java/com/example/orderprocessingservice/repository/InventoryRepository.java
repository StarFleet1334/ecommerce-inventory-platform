package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, String>{

    void deleteByProductId(String productId);
}
