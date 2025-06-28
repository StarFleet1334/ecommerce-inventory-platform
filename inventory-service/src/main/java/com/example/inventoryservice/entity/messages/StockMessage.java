package com.example.inventoryservice.entity.messages;

import lombok.Data;

@Data
public class StockMessage {

    private int ware_house_id;
    private String product_id;
    private int quantity;

}
