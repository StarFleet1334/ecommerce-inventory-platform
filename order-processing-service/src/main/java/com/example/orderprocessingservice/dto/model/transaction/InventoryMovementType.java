package com.example.orderprocessingservice.dto.model.transaction;

import com.example.orderprocessingservice.utils.constants.InventoryMovementTypeConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_movement_type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovementType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_type_id", nullable = false)
    private int id;

    @Column(name = "type_code", nullable = false, unique = true, length = InventoryMovementTypeConstants.TYPE_CODE_LENGTH)
    private String type_code;

    @Column(name = "type_name", nullable = false, length = InventoryMovementTypeConstants.TYPE_NAME_LENGTH)
    private String type_name;

    @Column(name = "affect_stock", nullable = false)
    private int affect_stock;

    @Column(name = "text")
    private String text;

    @Column(name = "is_active")
    private boolean active;

}
