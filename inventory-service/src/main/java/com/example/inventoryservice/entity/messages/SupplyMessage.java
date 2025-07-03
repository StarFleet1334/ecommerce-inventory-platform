package com.example.inventoryservice.entity.messages;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SupplyMessage {
    private int supplier_id;
    private String product_id;
    private int employee_id;
    private OffsetDateTime supply_time;
    private int amount;
}
