package com.example.inventoryservice.entity.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SupplyMessage {
    private int supplier_id;
    private String product_id;
    private int employee_id;
    @Schema(hidden = true)
    private OffsetDateTime supply_time;
    private int amount;
}
