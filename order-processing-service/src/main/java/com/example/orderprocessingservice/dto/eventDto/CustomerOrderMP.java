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
public class CustomerOrderMP {
    private String product_id;
    private int product_amount;
    private OffsetDateTime order_time;
    private int customer_id;
}
