package com.example.inventoryservice.entity.messages;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SupplierMessage {

    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
