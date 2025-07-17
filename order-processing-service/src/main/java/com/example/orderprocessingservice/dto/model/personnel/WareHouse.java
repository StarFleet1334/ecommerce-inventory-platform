package com.example.orderprocessingservice.dto.model.personnel;

import com.example.orderprocessingservice.utils.constants.WareHouseConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "ware_house")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WareHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ware_house_id")
    private int wareHouseId;

    @Column(name = "ware_house_name",unique = true,nullable = false,length = WareHouseConstants.WAREHOUSE_NAME_LENGTH)
    @Size(max = WareHouseConstants.WAREHOUSE_NAME_LENGTH)
    @JsonProperty("ware_house_name")
    private String wareHouseName;

    @Column(name = "ware_house_capacity",nullable = false)
    @JsonProperty("ware_house_capacity")
    private int wareHouseCapacity;

    @Column(name = "is_refrigerated")
    @JsonProperty("is_refrigerated")
    private boolean refrigerated;

    @Column(name = "min_stock_level",nullable = false)
    @JsonProperty("min_stock_level")
    private int minStockLevel;

    @Column(name = "max_stock_level",nullable = false)
    @JsonProperty("max_stock_level")
    private int maxStockLevel;

    @Column(name = "latitude",nullable = false,precision = WareHouseConstants.LATITUDE_PRECISION,scale = WareHouseConstants.LATITUDE_SCALE)
    @Digits(integer = WareHouseConstants.LATITUDE_DIGITS_INTEGER,fraction = WareHouseConstants.LATITUDE_DIGITS_FRACTIONAL)
    @DecimalMin(value = WareHouseConstants.LATITUDE_MIN_DECIMAL)
    @DecimalMax(value = WareHouseConstants.LATITUDE_MAX_DECIMAL)
    private BigDecimal latitude;

    @Column(name = "longitude",nullable = false,precision = WareHouseConstants.LONGITUDE_PRECISION,scale = WareHouseConstants.LONGITUDE_SCALE)
    @Digits(integer = WareHouseConstants.LONGITUDE_DIGITS_INTEGER,fraction = WareHouseConstants.LONGITUDE_DIGITS_FRACTIONAL)
    @DecimalMin(value = WareHouseConstants.LONGITUDE_MIN_DECIMAL)
    @DecimalMax(value = WareHouseConstants.LONGITUDE_MAX_DECIMAL)
    private BigDecimal longitude;

    @OneToMany(mappedBy = "wareHouse")
    private List<Employee> employeeList;

}
