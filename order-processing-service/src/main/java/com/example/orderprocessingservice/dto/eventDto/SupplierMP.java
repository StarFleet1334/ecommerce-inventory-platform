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
public class SupplierMP {

    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
