package com.example.orderprocessingservice.dto.mapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMP {

    private int ware_house_id;
    private String product_id;
    private int quantity;
}
