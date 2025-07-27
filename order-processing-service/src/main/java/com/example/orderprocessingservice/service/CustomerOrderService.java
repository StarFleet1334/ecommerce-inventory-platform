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
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOrderService.class);
    private final OrderRepository orderRepository;
    private final CustomerTransactionRepository customerTransactionRepository;
    private final CustomerInventoryRepository customerInventoryRepository;

    @Transactional
    public void speedUpCustomerOrder(int orderId) {
        LOGGER.info("Speeding up customer order {}", orderId);

        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomerOrderException.notFound(orderId));

        CustomerTransaction tx = customerTransactionRepository
                .findByCustomerOrder_OrderId(orderId);

        if (tx.isFinished()) {
            LOGGER.info("Order {} is already finished", orderId);
            return;
        }
        tx.setFinished(true);
        customerTransactionRepository.save(tx);
        int delta           = order.getProductAmount();
        int customerId      = order.getCustomer().getCustomer_id();
        String productId    = order.getProduct().getProduct_id();
        OffsetDateTime now  = OffsetDateTime.now();

        customerInventoryRepository.upsertQuantity(customerId, productId, delta, now);

        LOGGER.info("Inventory updated for customer {} / product {} (+{})", customerId, productId, delta);

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
