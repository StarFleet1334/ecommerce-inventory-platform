package com.example.inventoryservice.entity.messages;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductMessage {

    private String product_name;
    private String sku;
    private String product_id;
    private BigDecimal product_price;
    private String product_description;
}
