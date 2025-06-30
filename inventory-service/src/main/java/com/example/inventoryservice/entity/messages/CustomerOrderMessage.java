package com.example.inventoryservice.entity.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class CustomerOrderMessage {
    private String product_id;
    private int product_amount;
    @Schema(hidden = true)
    private OffsetDateTime order_time;
    private int customer_id;
}
