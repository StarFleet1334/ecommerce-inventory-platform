package com.example.orderprocessingservice.dto.model.transaction;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "ware_house_id",nullable = false)
    private WareHouse wareHouse;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "movement_type_id", referencedColumnName = "movement_type_id")
    private InventoryMovementType inventoryMovementType;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Column(name = "quantity",nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost", precision = 10, scale = 4)
    private BigDecimal unitCost;

    @Column(name = "batch_number", length = 50)
    private String batch_number;

    @Column(name = "serial_number", length = 100)
    private String serial_number;

    @Column(name = "expiry_date")
    private LocalDateTime expiry_date;

    @Column(name = "reference_type",length = 30)
    private String reference_type;

    @Column(name = "reference_id")
    private Integer reference_id;

    @Column(name = "reference_number",length = 50)
    private String reference_number;

    @ManyToOne
    @JoinColumn(name = "from_warehouse_id", referencedColumnName = "ware_house_id")
    private WareHouse from_warehouse;

    @ManyToOne
    @JoinColumn(name = "to_warehouse_id", referencedColumnName = "ware_house_id")
    private WareHouse to_warehouse;

    @Column(name = "location_code",length = 20)
    private String location_code;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "employee_id")
    private Employee created_by;

    @Column(name = "reason_code",length = 20)
    private String reason_code;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_reversed", nullable = false)
    @Builder.Default
    private Boolean isReversed = false;

    @Column(name = "reversed_by_movement_id")
    private Integer reversedByMovementId;


}
