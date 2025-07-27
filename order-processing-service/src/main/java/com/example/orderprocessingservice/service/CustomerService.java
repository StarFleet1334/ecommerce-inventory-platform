package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.CustomerMP;
import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.customer.CustomerInventory;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.personnel.WareHouseInventoryBacklog;
import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import com.example.orderprocessingservice.dto.model.transaction.InventoryMovement;
import com.example.orderprocessingservice.exception.asset.StockException;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.mapper.customer.CustomerMapper;
import com.example.orderprocessingservice.mapper.customer.CustomerOrderMapper;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.customer.CustomerInventoryRepository;
import com.example.orderprocessingservice.repository.customer.CustomerOrderRepository;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseInventoryBacklogRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.transaction.CustomerTransactionRepository;
import com.example.orderprocessingservice.repository.transaction.InventoryMovementRepository;
import com.example.orderprocessingservice.repository.transaction.InventoryMovementTypeRepository;
import com.example.orderprocessingservice.service.domain.TransactionService;
import com.example.orderprocessingservice.utils.InventoryMovementReferenceType;
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

    // Repository dependencies
    private final CustomerRepository customerRepository;
    private final CustomerTransactionRepository customerTransactionRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final WareHouseRepository wareHouseRepository;
    private final StockRepository stockRepository;
    private final CustomerInventoryRepository customerInventoryRepository;
    private final ProductRepository productRepository;
    private final WareHouseInventoryBacklogRepository wareHouseInventoryBacklogRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryMovementTypeRepository inventoryMovementTypeRepository;
    private final EmployeeRepository employeeRepository;

    // Service dependencies
    private final TransactionService transactionService;

    // Validator dependencies
    private final CustomerValidator customerValidator;
    private final CustomerOrderValidator customerOrderValidator;

    // Mapper dependencies
    private final CustomerMapper customerMapper;
    private final CustomerOrderMapper customerOrderMapper;

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
            validateCustomerExists(customerId);

            deleteCustomerRelatedData(customerId);
            customerRepository.deleteById(customerId);
            LOGGER.info("Successfully deleted customer with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid customer ID format: " + id);
        }
    }

    @Transactional
    public void handleCustomerNewOrder(CustomerOrderMP customerOrderRequest) {
        LOGGER.info("Processing new customer order: {}", customerOrderRequest);
        customerOrderValidator.validate(customerOrderRequest);
        // Step 1: Validate and fetch required data
        OrderContext context = buildOrderContext(customerOrderRequest);
        // Step 2: Process stock allocation
        StockAllocationResult allocationResult = allocateStock(context.availableStocks, customerOrderRequest.getProduct_amount());
        // Step 3: Create and save a customer order
        CustomerOrder savedOrder = createAndSaveCustomerOrder(customerOrderRequest, allocationResult);
        // Step 4: Create a customer transaction
        createCustomerTransaction(savedOrder, context.customer, allocationResult.getUsedWarehouses());
        // Step 5: Handle remaining stock (backlog)
        handleStockBacklog(allocationResult.getRemainingAmount(), context.product, context.warehouse, savedOrder);
        // Step 6: Record inventory movement
        recordInventoryMovement(customerOrderRequest, context.product, context.warehouse, context.customer);
    }

    private OrderContext buildOrderContext(CustomerOrderMP customerOrderRequest) {
        List<Stock> availableStocks = stockRepository.findAllByProductId(customerOrderRequest.getProduct_id());
        if (availableStocks.isEmpty()) {
            LOGGER.warn("No stocks found for product ID {}", customerOrderRequest.getProduct_id());
            throw StockException.notFound(customerOrderRequest.getProduct_id());
        }

        Product product = productRepository.findByProductId(customerOrderRequest.getProduct_id());
        Optional<Customer> customer = customerRepository.findById(customerOrderRequest.getCustomer_id());
        if (customer.isEmpty()) {
            throw CustomerException.notFound(customerOrderRequest.getCustomer_id());
        }

        // TODO: This needs to be changed, we need to pass in request body from which ware_house we take in future
        Stock primaryStock = availableStocks.get(new Random().nextInt(availableStocks.size()));
        Optional<WareHouse> warehouse = wareHouseRepository.findById(primaryStock.getWareHouse().getWareHouseId());

        return OrderContext.builder()
                .availableStocks(availableStocks)
                .product(product)
                .customer(customer.get())
                .warehouse(warehouse.orElse(null))
                .build();
    }

    private StockAllocationResult allocateStock(List<Stock> stockList, int requestedAmount) {
        LOGGER.debug("Allocating stock for requested amount: {}", requestedAmount);

        Stock selectedStock = stockList.get(new Random().nextInt(stockList.size()));
        List<Stock> usedStocks = new ArrayList<>();
        int remainingAmount = requestedAmount;

        int availableQuantity = selectedStock.getQuantity();
        if (availableQuantity > 0) {
            int quantityToTake = Math.min(availableQuantity, remainingAmount);

            updateStockQuantity(selectedStock, quantityToTake);
            usedStocks.add(selectedStock);
            remainingAmount -= quantityToTake;

            LOGGER.debug("Allocated {} units from stock, remaining: {}", quantityToTake, remainingAmount);
        }

        List<WareHouse> usedWarehouses = usedStocks.stream()
                .map(Stock::getWareHouse)
                .collect(Collectors.toList());

        return StockAllocationResult.builder()
                .usedStocks(usedStocks)
                .usedWarehouses(usedWarehouses)
                .remainingAmount(remainingAmount)
                .allocatedAmount(requestedAmount - remainingAmount)
                .build();
    }

    private void updateStockQuantity(Stock stock, int quantityToTake) {
        stock.setQuantity(stock.getQuantity() - quantityToTake);
        stock.getWareHouse().setWareHouseCapacity(
                stock.getWareHouse().getWareHouseCapacity() - quantityToTake
        );

        if (stock.getQuantity() <= 0) {
            stockRepository.delete(stock);
            LOGGER.debug("Deleted empty stock for product: {}", stock.getProduct().getProduct_id());
        } else {
            stockRepository.save(stock);
        }

        wareHouseRepository.save(stock.getWareHouse());
    }

    private CustomerOrder createAndSaveCustomerOrder(CustomerOrderMP customerOrderRequest, StockAllocationResult allocationResult) {
        CustomerOrder newCustomerOrder = customerOrderMapper.toEntity(customerOrderRequest);

        if (allocationResult.getRemainingAmount() > 0) {
            newCustomerOrder.setProductAmount(allocationResult.getAllocatedAmount());
        }

        try {
            customerOrderRepository.save(newCustomerOrder);
            LOGGER.info("Successfully saved customer order with allocated amount: {}", allocationResult.getAllocatedAmount());
            return newCustomerOrder;
        } catch (Exception e) {
            LOGGER.error("Failed to save customer order: {}", e.getMessage());
            throw new RuntimeException("Failed to save customer order", e);
        }
    }

    private void createCustomerTransaction(CustomerOrder order, Customer customer, List<WareHouse> warehouses) {
        try {
            CustomerTransaction transaction = transactionService.create(order, customer, warehouses);
            LOGGER.info("Created customer transaction with expected delivery time: {}",
                    transaction.getExpected_delivery_time());
        } catch (Exception e) {
            LOGGER.error("Failed to create customer transaction: {}", e.getMessage());
            throw new RuntimeException("Failed to create customer transaction", e);
        }
    }

    private void handleStockBacklog(int remainingAmount, Product product, WareHouse warehouse, CustomerOrder order) {
        if (remainingAmount <= 0) {
            return;
        }

        LOGGER.info("Creating backlog for remaining amount: {} of product: {}", remainingAmount, product.getProduct_id());

        WareHouseInventoryBacklog backlog = WareHouseInventoryBacklog.builder()
                .wareHouse(warehouse)
                .product(product)
                .customerOrder(order)
                .debtQuantity(remainingAmount)
                .recordedAt(OffsetDateTime.now())
                .lastUpdatedAt(OffsetDateTime.now())
                .build();

        wareHouseInventoryBacklogRepository.save(backlog);
        LOGGER.info("Successfully created inventory backlog with ID: {}", backlog.getId());
    }

    private void recordInventoryMovement(CustomerOrderMP orderRequest, Product product, WareHouse warehouse, Customer customer) {
        try {
            List<Employee> employees = employeeRepository.findAllEmployees();
            Employee randomEmployee = employees.get(new Random().nextInt(employees.size()));

            InventoryMovement movement = InventoryMovement.builder()
                    .wareHouse(warehouse)
                    .product(product)
                    .inventoryMovementType(inventoryMovementTypeRepository.findByInventoryMovementTypeId(
                            InventoryMovementReferenceType.SALE.getType_id()))
                    .customer(customer)
                    .quantity(orderRequest.getProduct_amount())
                    .unitCost(product.getProduct_price())
                    .reference_type(InventoryMovementReferenceType.CUSTOMER_ORDER.getValue())
                    .employee(randomEmployee)
                    .notes("Customer order: " + orderRequest.getProduct_amount() + " products")
                    .build();

            inventoryMovementRepository.save(movement);
            LOGGER.info("Successfully recorded inventory movement with ID: {}", movement.getId());
        } catch (Exception e) {
            LOGGER.error("Failed to record inventory movement: {}", e.getMessage());
            throw new RuntimeException("Failed to record inventory movement", e);
        }
    }

    private void validateCustomerExists(int customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw CustomerException.notFound(customerId);
        }
    }

    private void deleteCustomerRelatedData(int customerId) {
        List<CustomerOrder> customerOrders = customerOrderRepository.findByCustomerId(customerId);
        for (CustomerOrder order : customerOrders) {
            CustomerTransaction transaction = customerTransactionRepository.findByCustomerOrder_OrderId(order.getOrderId());
            if (transaction != null) {
                customerTransactionRepository.delete(transaction);
            }
        }
        LOGGER.info("Successfully deleted customer transactions for customer ID: {}", customerId);

        customerOrderRepository.deleteAll(customerOrders);
        LOGGER.info("Successfully deleted customer orders for customer ID: {}", customerId);

        List<CustomerInventory> customerInventories = customerInventoryRepository.findAllByCustomerId(customerId);
        if (!customerInventories.isEmpty()) {
            customerInventoryRepository.deleteAll(customerInventories);
            LOGGER.info("Successfully deleted customer inventories for customer ID: {}", customerId);
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

    @lombok.Builder
    @lombok.Data
    private static class OrderContext {
        private List<Stock> availableStocks;
        private Product product;
        private Customer customer;
        private WareHouse warehouse;
    }

    @lombok.Builder
    @lombok.Data
    private static class StockAllocationResult {
        private List<Stock> usedStocks;
        private List<WareHouse> usedWarehouses;
        private int remainingAmount;
        private int allocatedAmount;
    }
}