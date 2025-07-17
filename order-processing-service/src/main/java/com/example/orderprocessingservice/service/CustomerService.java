package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.CustomerMP;
import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.dto.model.order.RouteCalculationResponse;
import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.mapper.customer.CustomerMapper;
import com.example.orderprocessingservice.mapper.customer.CustomerOrderMapper;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.customer.CustomerOrderRepository;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.transaction.CustomerTransactionRepository;
import com.example.orderprocessingservice.validator.CustomerOrderValidator;
import com.example.orderprocessingservice.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final CustomerTransactionRepository customerTransactionRepository;
    private final CustomerValidator customerValidator;
    private final CustomerOrderValidator customerOrderValidator;
    private final CustomerMapper customerMapper;
    private final CustomerOrderMapper customerOrderMapper;
    private final CustomerOrderRepository customerOrderRepository;
    private final WareHouseRepository wareHouseRepository;
    private final StockRepository stockRepository;
    private final RouteCalculationService routeCalculationService;

    @Transactional
    public void handleNewCustomer(CustomerMP customer) {
        LOGGER.info("Processing new customer: {}", customer);

        customerValidator.validate(customer);

        Customer newCustomer = customerMapper.toEntity(customer);

        try {
            customerRepository.save(newCustomer);
            LOGGER.info("Successfully saved new customer with email: {}", customer.getEmail());
        } catch (Exception e) {
            LOGGER.error("Failed to save customer: {}", e.getMessage());
            throw new RuntimeException("Failed to save customer", e);
        }
    }

    @Transactional
    public void handleDeleteCustomer(String id) {
        LOGGER.info("Processing customer deletion for ID: {}", id);
        try {
            int customerId = Integer.parseInt(id);
            if (!customerRepository.existsById(customerId)) {
                throw CustomerException.notFound(customerId);
            }
            customerRepository.deleteById(customerId);
            LOGGER.info("Successfully deleted customer with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid customer ID format: " + id);
        }
    }

    @Transactional
    public void handleCustomerNewOrder(CustomerOrderMP customerOrder) {
        LOGGER.info("Processing new customer order: {}", customerOrder);
        customerOrderValidator.validate(customerOrder);

        List<Stock> stockList = stockRepository.findAllByProductId(customerOrder.getProduct_id());
        int remainingAmount = customerOrder.getProduct_amount();
        List<Stock> usedStocks = new ArrayList<>();

        for (Stock stock : stockList) {
            if (remainingAmount <= 0) break;

            int availableQuantity = stock.getQuantity();
            if (availableQuantity > 0) {
                int quantityToTake = Math.min(availableQuantity, remainingAmount);

                stock.setQuantity(availableQuantity - quantityToTake);
                stock.getWareHouse().setWareHouseCapacity(
                        stock.getWareHouse().getWareHouseCapacity() - quantityToTake
                );

                if (stock.getQuantity() <= 0) {
                    stockRepository.delete(stock);
                } else {
                    stockRepository.save(stock);
                }
                wareHouseRepository.save(stock.getWareHouse());

                usedStocks.add(stock);
                remainingAmount -= quantityToTake;
            }
        }

        if (remainingAmount > 0) {
            // <THIS TO BE IMPLEMENTED>
        }

        CustomerOrder newCustomerOrder = customerOrderMapper.toEntity(customerOrder);

        try {
            customerOrderRepository.save(newCustomerOrder);
            LOGGER.info("Successfully saved new customer order: {}", customerOrder);

            Set<Pair<BigDecimal, BigDecimal>> wareHouseList = usedStocks.stream()
                    .map(Stock::getWareHouse)
                    .map(wh -> Pair.of(wh.getLatitude(), wh.getLongitude()))
                    .collect(Collectors.toCollection(HashSet::new));


            Optional<Customer> customer = customerRepository.findById(customerOrder.getCustomer_id());
            if (customer.isEmpty()) {
                throw CustomerException.notFound(customerOrder.getCustomer_id());
            }

            RouteCalculationResponse bestRoute = null;
            BigDecimal customerLat = customer.get().getLatitude();
            BigDecimal customerLon = customer.get().getLongitude();

            for (Pair<BigDecimal, BigDecimal> warehouse : wareHouseList) {
                RouteCalculationResponse route = routeCalculationService.calculateRoute(
                        customerLat, customerLon, warehouse.getLeft(), warehouse.getRight());

                if (bestRoute == null || route.getDistanceKm() < bestRoute.getDistanceKm()) {
                    bestRoute = route;
                }
            }
            LOGGER.info("Best route for customer order {} is {}", customerOrder, bestRoute);
            CustomerTransaction customerTransaction = new CustomerTransaction();
            customerTransaction.setCustomerOrder(newCustomerOrder);

            if (bestRoute != null) {
                OffsetDateTime now = OffsetDateTime.now();
                OffsetDateTime expectedDeliveryTime = now.plus(Duration.ofSeconds((long) bestRoute.getDurationSeconds()));
                customerTransaction.setExpected_delivery_time(expectedDeliveryTime);
            } else {
                customerTransaction.setExpected_delivery_time(OffsetDateTime.now().plusHours(24));
                LOGGER.warn("No route found, setting default delivery time for order: {}", customerOrder);
            }

            customerTransaction.setFinished(false);

            customerTransactionRepository.save(customerTransaction);
            LOGGER.info("Created customer transaction with expected delivery time: {}",
                    customerTransaction.getExpected_delivery_time());

        } catch (Exception e) {
            LOGGER.error("Failed to save customer order: {}", e.getMessage());
            throw new RuntimeException("Failed to save customer order", e);
        }
    }

}
