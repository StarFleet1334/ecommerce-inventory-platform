package com.example.orderprocessingservice.repository.transaction;

import com.example.orderprocessingservice.dto.model.transaction.InventoryMovementType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementTypeRepository extends JpaRepository<InventoryMovementType, Integer> {
}
