package com.example.inventoryservice.entity.response;

import com.example.inventoryservice.entity.common.InventoryItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class InventoryItemResponse extends InventoryItem {
    private String id;
    private LocalDateTime lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InventoryItemResponse that = (InventoryItemResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, lastUpdated);
    }
}
