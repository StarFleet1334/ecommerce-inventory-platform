package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
