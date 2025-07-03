package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import com.example.orderprocessingservice.exception.customer.CustomerOrderException;
import com.example.orderprocessingservice.repository.order.OrderRepository;
import com.example.orderprocessingservice.repository.transaction.CustomerTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOrderService.class);
    private final OrderRepository orderRepository;
    private final CustomerTransactionRepository customerTransactionRepository;

    public void speedUpCustomerOrder(int orderId) {
        LOGGER.info("Speeding up customer order with ID: {}", orderId);
        Optional<CustomerOrder> customerOrder = orderRepository.findById(orderId);
        if (customerOrder.isEmpty()) {
            throw CustomerOrderException.notFound(orderId);
        }
        CustomerTransaction customerTransaction = customerTransactionRepository.findByCustomerOrder_OrderId(orderId);
        if (customerTransaction.isFinished()) {
            LOGGER.info("Customer order with ID {} is already finished", orderId);
            return;
        }
        customerTransaction.setFinished(true);
        customerTransactionRepository.save(customerTransaction);
        LOGGER.info("Successfully finished speedUp of customer order with ID {}", orderId);
    }
}
