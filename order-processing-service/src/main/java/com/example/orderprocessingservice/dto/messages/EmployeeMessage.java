package com.example.orderprocessingservice.dto.messages;

import lombok.Data;

@Data
public class EmployeeMessage {

    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private int ware_house_id;

}
