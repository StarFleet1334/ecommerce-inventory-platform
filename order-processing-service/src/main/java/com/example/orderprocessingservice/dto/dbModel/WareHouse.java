package com.example.orderprocessingservice.dto.dbModel;

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
import java.util.List;

@Entity
@Table(name = "WareHouse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WareHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wareHouse_id")
    private int wareHouse_id;

    @Column(name = "wareHouse_name",unique = true,nullable = false,length = 20)
    @Size(max = 20)
    private String wareHouse_name;

    @Column(name = "wareHouse_capacity",nullable = false)
    private int wareHouse_capacity;

    @Column(name = "isRefrigerated")
    private boolean isRefrigerated;

    @Column(name = "min_stock_level",nullable = false)
    private int min_stock_level;

    @Column(name = "max_stock_level",nullable = false)
    private int max_stock_level;

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

    @OneToMany(mappedBy = "wareHouse")
    private List<Employee> employeeList;

}
