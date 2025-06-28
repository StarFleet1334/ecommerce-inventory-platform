package com.example.orderprocessingservice.dto.mapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WareHouseMP {

    private String ware_house_name;
    private boolean is_refrigerated;
    private int mis_stock_level;
    private int max_stock_level;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
