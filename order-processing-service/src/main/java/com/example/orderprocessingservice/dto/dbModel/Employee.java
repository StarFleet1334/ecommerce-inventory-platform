package com.example.orderprocessingservice.dto.dbModel;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Employee")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int employee_id;

    @Column(name = "firstName",nullable = false,length = 10)
    @Size(max = 10)
    private String firstName;

    @Column(name = "lastName",nullable = false,length = 10)
    @Size(max = 10)
    private String lastName;

    @Column(name = "phoneNumber",nullable = false,length = 15,unique = true)
    @Size(max = 15)
    private String phoneNumber;

    @Column(name = "email",nullable = false,length = 50,unique = true)
    @Size(max = 50)
    private String email;

    @ManyToOne
    @JoinColumn(name = "wareHouse_id", nullable = false)
    private WareHouse wareHouse;

}
