package com.example.orderprocessingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMessage {
    private String productName;
    private String sku;
    private Integer quantity;
    private String warehouseLocation;
    private String id;
    private LocalDateTime lastUpdated;
}