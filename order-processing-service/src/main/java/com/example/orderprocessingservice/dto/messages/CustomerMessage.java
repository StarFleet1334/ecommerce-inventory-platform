package com.example.orderprocessingservice.dto.messages;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerMessage {

    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
