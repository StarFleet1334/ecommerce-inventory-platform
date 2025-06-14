package com.example.orderprocessingservice.dto.dbModel;


import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Table(name = "Product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "product_name",nullable = false,length = 50)
    @Size(max = 50)
    private String productName;

    @Column(name = "sku",nullable = false)
    @Size(max = 255)
    private String sku;

    @Column(name = "product_id",nullable = false,unique = true)
    @Size(max = 255)
    private String productId;

    @Column(name = "product_price",nullable = false,precision = 4,scale = 1)
    @Digits(integer = 3,fraction = 1)
    private Double productPrice;

    @Column(name = "product_description",nullable = false,length = 200)
    @Size(max = 200)
    private String productDescription;

}
