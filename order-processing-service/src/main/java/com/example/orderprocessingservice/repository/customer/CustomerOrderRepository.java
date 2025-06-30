package com.example.orderprocessingservice.repository.customer;

import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {
}
