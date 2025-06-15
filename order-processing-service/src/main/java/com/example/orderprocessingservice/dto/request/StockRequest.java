package com.example.orderprocessingservice.dto.request;

import lombok.Data;

@Data
public class StockRequest {
    private int ware_house_id;
    private String product_id;
    private int quantity;


}
