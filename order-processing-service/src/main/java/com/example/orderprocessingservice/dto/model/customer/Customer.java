package com.example.orderprocessingservice.dto.model.customer;


import com.example.orderprocessingservice.utils.constants.CustomerConstants;
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

    @Column(name = "first_name",nullable = false,length = CustomerConstants.MAX_FIRST_NAME_LENGTH)
    @Size(max = CustomerConstants.MAX_FIRST_NAME_LENGTH)
    private String first_name;

    @Column(name = "last_name",nullable = false,length = CustomerConstants.MAX_LAST_NAME_LENGTH)
    @Size(max = CustomerConstants.MAX_LAST_NAME_LENGTH)
    private String last_name;

    @Column(name = "email",unique = true,nullable = false,length = CustomerConstants.MAX_EMAIL_LENGTH)
    @Size(max = CustomerConstants.MAX_EMAIL_LENGTH)
    private String email;

    @Column(name = "phone_number",unique = true,nullable = false,length = CustomerConstants.MAX_PHONE_NUMBER_LENGTH)
    @Size(max = CustomerConstants.MAX_PHONE_NUMBER_LENGTH)
    private String phone_number;

    @Column(name = "latitude",nullable = false,precision = CustomerConstants.LATITUDE_PRECISION,scale = CustomerConstants.LATITUDE_SCALE)
    @Digits(integer = CustomerConstants.LATITUDE_DIGITS_INTEGER,fraction = CustomerConstants.LATITUDE_DIGITS_FRACTIONAL)
    @DecimalMin(value = CustomerConstants.LATITUDE_MIN_DECIMAL)
    @DecimalMax(value = CustomerConstants.LATITUDE_MAX_DECIMAL)
    private BigDecimal latitude;

    @Column(name = "longitude",nullable = false,precision = CustomerConstants.LONGITUDE_PRECISION,scale = CustomerConstants.LONGITUDE_SCALE)
    @Digits(integer = CustomerConstants.LONGITUDE_DIGITS_INTEGER,fraction = CustomerConstants.LONGITUDE_DIGITS_FRACTIONAL)
    @DecimalMin(value = CustomerConstants.LONGITUDE_MIN_DECIMAL)
    @DecimalMax(value = CustomerConstants.LONGITUDE_MAX_DECIMAL)
    private BigDecimal longitude;


}
