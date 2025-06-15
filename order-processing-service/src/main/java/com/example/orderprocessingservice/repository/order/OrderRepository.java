package com.example.orderprocessingservice.repository.order;

import com.example.orderprocessingservice.dto.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
