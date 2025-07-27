package com.example.orderprocessingservice.repository.transaction;

import com.example.orderprocessingservice.dto.model.transaction.InventoryMovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryMovementTypeRepository extends JpaRepository<InventoryMovementType, Integer> {

    @Query("SELECT imt FROM InventoryMovementType imt WHERE imt.id = :inventoryMovementTypeId")
    InventoryMovementType findByInventoryMovementTypeId(int inventoryMovementTypeId);
}
