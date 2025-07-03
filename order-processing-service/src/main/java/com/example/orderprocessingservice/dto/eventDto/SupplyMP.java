package com.example.orderprocessingservice.dto.eventDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyMP {
    private int supplier_id;
    private String product_id;
    private int employee_id;
    private OffsetDateTime supply_time;
    private int amount;
}
