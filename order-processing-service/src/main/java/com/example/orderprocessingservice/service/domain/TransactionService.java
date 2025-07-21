package com.example.orderprocessingservice.service.domain;

import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.dto.model.order.RouteCalculationResponse;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import com.example.orderprocessingservice.repository.transaction.CustomerTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private final CustomerTransactionRepository customerTransactionRepository;
    private final DeliveryEstimator deliveryEstimator;

    @Transactional
    public CustomerTransaction create(CustomerOrder order, Customer customer, Collection<WareHouse> sources) {
        RouteCalculationResponse bestRoute = deliveryEstimator.bestRoute(customer.getLatitude(), customer.getLatitude(), sources);
        LOGGER.info("Best route for customer order {} is {}", customer, bestRoute);
        CustomerTransaction customerTransaction = new CustomerTransaction();
        customerTransaction.setCustomerOrder(order);
        customerTransaction.setExpected_delivery_time(deliveryEstimator.expectedArrival(bestRoute));
        customerTransaction.setFinished(false);
        customerTransactionRepository.save(customerTransaction);
        return customerTransaction;
    }
}
