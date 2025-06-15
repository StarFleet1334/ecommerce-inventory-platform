package com.example.orderprocessingservice.dto.model.customer;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customer_id;

    @Column(name = "first_name",nullable = false,length = 10)
    @Size(max = 10)
    private String first_name;

    @Column(name = "last_name",nullable = false,length = 10)
    @Size(max = 10)
    private String last_name;

    @Column(name = "email",unique = true,nullable = false,length = 50)
    @Size(max = 50)
    private String email;

    @Column(name = "phone_number",unique = true,nullable = false,length = 10)
    @Size(max = 15)
    private String phone_number;

    @Column(name = "latitude",nullable = false,precision = 10,scale = 8)
    @Digits(integer = 2,fraction = 8)
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private BigDecimal latitude;

    @Column(name = "longitude",nullable = false,precision = 11,scale = 8)
    @Digits(integer = 3,fraction = 8)
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private BigDecimal longitude;


}
