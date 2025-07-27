package com.example.orderprocessingservice.repository.transaction;

import com.example.orderprocessingservice.dto.model.transaction.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Integer> {
}
