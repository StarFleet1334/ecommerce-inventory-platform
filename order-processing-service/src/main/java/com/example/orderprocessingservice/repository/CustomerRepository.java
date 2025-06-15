package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
