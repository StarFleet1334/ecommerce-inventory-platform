package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.CustomerMP;
import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.customer.CustomerInventory;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.personnel.WareHouseInventoryBacklog;
import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.mapper.customer.CustomerMapper;
import com.example.orderprocessingservice.mapper.customer.CustomerOrderMapper;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.customer.CustomerInventoryRepository;
import com.example.orderprocessingservice.repository.customer.CustomerOrderRepository;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseInventoryBacklogRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.transaction.CustomerTransactionRepository;
import com.example.orderprocessingservice.service.domain.TransactionService;
import com.example.orderprocessingservice.validator.CustomerOrderValidator;
import com.example.orderprocessingservice.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CustomerInventoryRepository customerInventoryRepository;
    private final ProductRepository productRepository;
    private final WareHouseInventoryBacklogRepository wareHouseInventoryBacklogRepository;
    private final TransactionService transactionService;

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
            // 1st step
            List<CustomerOrder> customerOrder = customerOrderRepository.findByCustomerId(customerId);
            LOGGER.info("Customer order for customer with ID {} is {}", customerId, customerOrder);

            for (CustomerOrder order : customerOrder) {
                CustomerTransaction transaction = customerTransactionRepository.findByCustomerOrder_OrderId(order.getOrderId());
                customerTransactionRepository.delete(transaction);
            }

            // 2nd step
            LOGGER.info("Successfully deleted customer transactions for customer with ID {}", customerId);
            customerOrderRepository.deleteAll(customerOrder);
            LOGGER.info("Successfully deleted customer orders for customer with ID {}", customerId);

            // 3rd step
            List<CustomerInventory> customerInventories = customerInventoryRepository.findAllByCustomerId(customerId);
            if (!customerInventories.isEmpty()) {
                customerInventoryRepository.deleteAll(customerInventories);
            }

            // 4th step
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
        int wareHouseId = 0;

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
                wareHouseId = stock.getWareHouse().getWareHouseId();
                wareHouseRepository.save(stock.getWareHouse());

                usedStocks.add(stock);
                remainingAmount -= quantityToTake;
            }
        }

        CustomerOrder newCustomerOrder = customerOrderMapper.toEntity(customerOrder);
        if (remainingAmount > 0) {
            newCustomerOrder.setProductAmount(customerOrder.getProduct_amount() - remainingAmount);
        }

        try {
            customerOrderRepository.save(newCustomerOrder);
            LOGGER.info("Successfully saved new customer order: {}", customerOrder);

            List<WareHouse> wareHouseList = usedStocks.stream()
                    .map(Stock::getWareHouse).collect(Collectors.toList());

            Optional<Customer> customer = customerRepository.findById(customerOrder.getCustomer_id());
            if (customer.isEmpty()) {
                throw CustomerException.notFound(customerOrder.getCustomer_id());
            }
            CustomerTransaction customerTransaction = transactionService.create(newCustomerOrder, customer.get(), wareHouseList);
            LOGGER.info("Created customer transaction with expected delivery time: {}",
                    customerTransaction.getExpected_delivery_time());

        } catch (Exception e) {
            LOGGER.error("Failed to save customer order: {}", e.getMessage());
            throw new RuntimeException("Failed to save customer order", e);
        } finally {
            // STAGE 2
            if (!stockList.isEmpty()) {
                if (remainingAmount > 0) {
                    Optional<WareHouse> wareHouse = wareHouseRepository.findById(wareHouseId);
                    Product product = productRepository.findByProductId(customerOrder.getProduct_id());
                    WareHouseInventoryBacklog wareHouseInventoryBacklog = new WareHouseInventoryBacklog();
                    LOGGER.info("Creating new WareHouseInventoryBacklog for product {} in warehouse {}", product, wareHouse);
                    wareHouseInventoryBacklog.setWareHouse(wareHouse.get());
                    wareHouseInventoryBacklog.setProduct(product);
                    wareHouseInventoryBacklog.setCustomerOrder(newCustomerOrder);
                    wareHouseInventoryBacklog.setDebtQuantity(remainingAmount);
                    wareHouseInventoryBacklog.setRecordedAt(OffsetDateTime.now());
                    wareHouseInventoryBacklog.setLastUpdatedAt(OffsetDateTime.now());
                    LOGGER.info("Saving WareHouseInventoryBacklog: {}", wareHouseInventoryBacklog);
                    wareHouseInventoryBacklogRepository.save(wareHouseInventoryBacklog);
                    LOGGER.info("Successfully saved new WareHouseInventoryBacklog: {}", wareHouseInventoryBacklog);
                }
            }
        }
    }

    public Customer getCustomerById(int id) {
        LOGGER.info("Fetching customer with ID: {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            LOGGER.error("Customer not found with ID: {}", id);
            throw CustomerException.notFound(id);
        }
        LOGGER.debug("Customer found successfully");
        return customer.get();
    }

    public List<Customer> getAllCustomers() {
        LOGGER.info("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();
        LOGGER.debug("Customers found successfully");
        return customers;
    }

}
