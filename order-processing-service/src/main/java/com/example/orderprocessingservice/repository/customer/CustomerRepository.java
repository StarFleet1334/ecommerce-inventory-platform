package com.example.orderprocessingservice.repository.customer;

import com.example.orderprocessingservice.dto.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCustomerId(String customerId);
}
