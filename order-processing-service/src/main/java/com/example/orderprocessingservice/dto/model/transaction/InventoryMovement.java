package com.example.orderprocessingservice.dto.model.transaction;

import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.utils.constants.InventoryMovementConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "ware_house_id", nullable = false)
    @ToString.Exclude
    private WareHouse wareHouse;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ToString.Exclude
    private Product product;

    @ManyToOne
    @JoinColumn(name = "movement_type_id", referencedColumnName = "movement_type_id")
    @ToString.Exclude
    private InventoryMovementType inventoryMovementType;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ToString.Exclude
    private Customer customer;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost", precision = InventoryMovementConstants.PRECISION, scale = InventoryMovementConstants.SCALE)
    private BigDecimal unitCost;

    @Column(name = "batch_number", length = InventoryMovementConstants.BATCH_NUMBER_LENGTH)
    private String batch_number;

    @Column(name = "serial_number", length = InventoryMovementConstants.SERIAL_NUMBER_LENGTH)
    private String serial_number;

    @Column(name = "expiry_date")
    private LocalDateTime expiry_date;

    @Column(name = "reference_type", length = InventoryMovementConstants.REFERENCE_TYPE_LENGTH)
    private String reference_type;

    @Column(name = "reference_id")
    private Integer reference_id;

    @Column(name = "reference_number", length = InventoryMovementConstants.REFERENCE_NUMBER_LENGTH)
    private String reference_number;

    @ManyToOne
    @JoinColumn(name = "from_warehouse_id", referencedColumnName = "ware_house_id")
    @ToString.Exclude
    private WareHouse from_warehouse;

    @ManyToOne
    @JoinColumn(name = "to_warehouse_id", referencedColumnName = "ware_house_id")
    @ToString.Exclude
    private WareHouse to_warehouse;

    @Column(name = "location_code", length = InventoryMovementConstants.LOCATION_CODE_LENGTH)
    private String location_code;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    @ToString.Exclude
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "employee_id")
    @ToString.Exclude
    private Employee created_by;

    @Column(name = "reason_code", length = InventoryMovementConstants.REASON_CODE_LENGTH)
    private String reason_code;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_reversed", nullable = false)
    @Builder.Default
    private Boolean isReversed = false;

    @Column(name = "reversed_by_movement_id")
    private Integer reversedByMovementId;
}