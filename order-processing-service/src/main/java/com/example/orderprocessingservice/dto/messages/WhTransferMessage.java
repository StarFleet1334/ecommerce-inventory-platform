package com.example.orderprocessingservice.dto.messages;

import lombok.Data;

@Data
public class WhTransferMessage {
    private Integer from_ware_house_id ;
    private Integer to_ware_house_id ;
    private String product_id ;
    private Integer quantity;
}
