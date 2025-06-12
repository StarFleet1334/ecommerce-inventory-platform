
package com.example.orderprocessingservice.dto.dbModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "warehouse_location")
    private String warehouseLocation;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}