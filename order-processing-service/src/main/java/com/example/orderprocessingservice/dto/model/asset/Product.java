package com.example.orderprocessingservice.dto.model.asset;


import com.example.orderprocessingservice.utils.constants.ProductConstants;
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

    @Column(name = "product_name",nullable = false,length = ProductConstants.MAX_PRODUCT_NAME_LENGTH)
    @JsonProperty("product_name")
    @Size(max = ProductConstants.MAX_PRODUCT_NAME_LENGTH)
    private String product_name;

    @Column(name = "sku",nullable = false)
    @JsonProperty("sku")
    @Size(max = ProductConstants.MAX_PRODUCT_SKU_LENGTH)
    private String sku;

    @Column(name = "product_id",nullable = false,unique = true)
    @JsonProperty("product_id")
    @Size(max = ProductConstants.MAX_PRODUCT_ID_LENGTH)
    private String product_id;

    @Column(name = "product_price",nullable = false,precision = ProductConstants.MAX_PRODUCT_PRICE_PRECISION,scale = ProductConstants.MAX_PRODUCT_PRICE_SCALE)
    @JsonProperty("product_price")
    @Digits(integer = ProductConstants.MAX_PRODUCT_PRICE_DIGITS_INTEGER,fraction = ProductConstants.MAX_PRODUCT_PRICE_DIGITS_FRACTIONAL)
    private BigDecimal product_price;

    @Column(name = "product_description",nullable = false,length = ProductConstants.MAX_PRODUCT_DESCRIPTION_LENGTH)
    @JsonProperty("product_description")
    @Size(max = ProductConstants.MAX_PRODUCT_DESCRIPTION_LENGTH)
    private String product_description;

}
