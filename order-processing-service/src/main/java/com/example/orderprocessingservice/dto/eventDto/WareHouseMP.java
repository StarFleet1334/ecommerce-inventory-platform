package com.example.orderprocessingservice.dto.eventDto;

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
    private boolean refrigerated;
    private int min_stock_level;
    private int max_stock_level;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
