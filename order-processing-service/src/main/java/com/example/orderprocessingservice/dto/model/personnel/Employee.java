package com.example.orderprocessingservice.dto.model.personnel;


import com.example.orderprocessingservice.utils.constants.EmployeeConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int employee_id;

    @Column(name = "first_name",nullable = false,length = EmployeeConstants.MAX_FIRST_NAME_LENGTH)
    @Size(max = EmployeeConstants.MAX_FIRST_NAME_LENGTH)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name",nullable = false,length = EmployeeConstants.MAX_LAST_NAME_LENGTH)
    @Size(max = EmployeeConstants.MAX_LAST_NAME_LENGTH)
    @JsonProperty("last_name")
    private String lastName;

    @Column(name = "phone_number",nullable = false,length = EmployeeConstants.MAX_PHONE_NUMBER_LENGTH,unique = true)
    @Size(max = EmployeeConstants.MAX_PHONE_NUMBER_LENGTH)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Column(name = "email",nullable = false,length = EmployeeConstants.MAX_EMAIL_LENGTH,unique = true)
    @Size(max = EmployeeConstants.MAX_EMAIL_LENGTH)
    private String email;

    @ManyToOne
    @JoinColumn(name = "ware_house_id", nullable = false)
    private WareHouse wareHouse;

    @Transient
    private Integer wareHouseId;

    @JsonProperty("ware_house_id")
    private void setWareHouseId(Integer id) {
        this.wareHouseId = id;
    }



}
