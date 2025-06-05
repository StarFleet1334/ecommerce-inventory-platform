package com.example.inventoryservice.entity.common;

import lombok.Data;

@Data
public class InventoryItem {
    private String productName;
    private String sku;
    private int quantity;
    private String warehouseLocation;
}
