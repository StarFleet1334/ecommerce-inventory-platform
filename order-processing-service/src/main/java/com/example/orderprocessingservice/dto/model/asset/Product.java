package com.example.orderprocessingservice.dto.model.asset;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "product_name",nullable = false,length = 50)
    @JsonProperty("product_name")
    @Size(max = 50)
    private String product_name;

    @Column(name = "sku",nullable = false)
    @JsonProperty("sku")
    @Size(max = 255)
    private String sku;

    @Column(name = "product_id",nullable = false,unique = true)
    @JsonProperty("product_id")
    @Size(max = 255)
    private String product_id;

    @Column(name = "product_price",nullable = false,precision = 4,scale = 1)
    @JsonProperty("product_price")
    @Digits(integer = 3,fraction = 1)
    private BigDecimal product_price;

    @Column(name = "product_description",nullable = false,length = 200)
    @JsonProperty("product_description")
    @Size(max = 200)
    private String product_description;

}
