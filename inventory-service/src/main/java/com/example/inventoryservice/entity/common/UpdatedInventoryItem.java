package com.example.inventoryservice.entity.common;

import lombok.Data;

@Data
public class UpdatedInventoryItem {
    private String id;
    private InventoryItem item;

    public UpdatedInventoryItem(String id, InventoryItem item) {
        this.id = id;
        this.item = item;
    }
}
