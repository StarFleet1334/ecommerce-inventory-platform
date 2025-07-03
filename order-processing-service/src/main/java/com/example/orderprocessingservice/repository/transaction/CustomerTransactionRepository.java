package com.example.orderprocessingservice.repository.transaction;

import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, Integer> {
    CustomerTransaction findByCustomerOrder_OrderId(int orderId);
}
