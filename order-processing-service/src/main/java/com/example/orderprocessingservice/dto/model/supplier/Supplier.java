package com.example.orderprocessingservice.dto.model.supplier;

import com.example.orderprocessingservice.utils.constants.SupplierConstants;
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
@Table(name = "supplier")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private int supplier_id;

    @Column(name = "first_name",unique = true,nullable = false,length = SupplierConstants.MAX_FIRST_NAME_LENGTH)
    @Size(max = SupplierConstants.MAX_FIRST_NAME_LENGTH)
    private String first_name;

    @Column(name = "last_name",unique = true,nullable = false,length = SupplierConstants.MAX_LAST_NAME_LENGTH)
    @Size(max = SupplierConstants.MAX_LAST_NAME_LENGTH)
    private String last_name;

    @Column(name = "email",unique = true,nullable = false,length = SupplierConstants.MAX_EMAIL_LENGTH)
    @Size(max = SupplierConstants.MAX_EMAIL_LENGTH)
    private String email;

    @Column(name = "phone_number",unique = true,nullable = false,length = SupplierConstants.MAX_PHONE_NUMBER_LENGTH)
    @Size(max = SupplierConstants.MAX_PHONE_NUMBER_LENGTH)
    private String phone_number;

    @Column(name = "latitude",nullable = false,precision = SupplierConstants.LATITUDE_PRECISION,scale = SupplierConstants.LATITUDE_SCALE)
    @Digits(integer = SupplierConstants.LATITUDE_DIGITS_INTEGER,fraction = SupplierConstants.LATITUDE_DIGITS_FRACTIONAL)
    @DecimalMin(value = SupplierConstants.LATITUDE_MIN_DECIMAL)
    @DecimalMax(value = SupplierConstants.LATITUDE_MAX_DECIMAL)
    private BigDecimal latitude;

    @Column(name = "longitude",nullable = false,precision = SupplierConstants.LONGITUDE_PRECISION,scale = SupplierConstants.LONGITUDE_SCALE)
    @Digits(integer = SupplierConstants.LONGITUDE_DIGITS_INTEGER,fraction = SupplierConstants.LONGITUDE_DIGITS_FRACTIONAL)
    @DecimalMin(value = SupplierConstants.LONGITUDE_MIN_DECIMAL)
    @DecimalMax(value = SupplierConstants.LONGITUDE_MAX_DECIMAL)
    private BigDecimal longitude;
}
