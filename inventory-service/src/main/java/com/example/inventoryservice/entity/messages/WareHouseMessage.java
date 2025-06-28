package com.example.inventoryservice.entity.messages;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WareHouseMessage {

    private String ware_house_name;
    private boolean is_refrigerated;
    private int mis_stock_level;
    private int max_stock_level;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
