package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.model.customer.CustomerInventory;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import com.example.orderprocessingservice.exception.customer.CustomerOrderException;
import com.example.orderprocessingservice.repository.customer.CustomerInventoryRepository;
import com.example.orderprocessingservice.repository.order.OrderRepository;
import com.example.orderprocessingservice.repository.transaction.CustomerTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOrderService.class);
    private final OrderRepository orderRepository;
    private final CustomerTransactionRepository customerTransactionRepository;
    private final CustomerInventoryRepository customerInventoryRepository;

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

        CustomerOrder order = customerOrder.get();
        Integer customerId = order.getCustomer().getCustomer_id();
        String productId = order.getProduct().getProduct_id();
        if (customerInventoryRepository.existsByCustomerIdAndProductId(customerId, productId)) {
            CustomerInventory existingInventory = customerInventoryRepository
                    .findByCustomerIdAndProductId(customerId, productId)
                    .orElseThrow();

            existingInventory.setQuantity(existingInventory.getQuantity() + order.getProductAmount());
            existingInventory.setLastUpdated(OffsetDateTime.now());
            customerInventoryRepository.save(existingInventory);
        } else {
            customerInventoryRepository.save(constructrCustomerInventory(order));
        }
        LOGGER.info("Successfully saved new customer inventory for order with ID: {}", orderId);

    }

    private CustomerInventory constructrCustomerInventory(CustomerOrder customerOrder) {
        CustomerInventory customerInventory = new CustomerInventory();
        customerInventory.setCustomerId(customerOrder.getCustomer().getCustomer_id());
        customerInventory.setProductId(customerOrder.getProduct().getProduct_id());
        customerInventory.setQuantity(customerOrder.getProductAmount());
        customerInventory.setLastUpdated(OffsetDateTime.now());
        return customerInventory;
    }
}
