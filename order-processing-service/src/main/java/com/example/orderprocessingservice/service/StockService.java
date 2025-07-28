package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
import com.example.orderprocessingservice.dto.eventDto.StockMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.asset.Supply;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.personnel.WareHouseInventoryBacklog;
import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import com.example.orderprocessingservice.dto.model.transaction.InventoryMovement;
import com.example.orderprocessingservice.dto.model.transaction.SupplyTransaction;
import com.example.orderprocessingservice.exception.asset.StockException;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.exception.personnel.WareHouseException;
import com.example.orderprocessingservice.mapper.customer.CustomerOrderMapper;
import com.example.orderprocessingservice.mapper.stock.StockMapper;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.customer.CustomerOrderRepository;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseInventoryBacklogRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.transaction.InventoryMovementRepository;
import com.example.orderprocessingservice.repository.transaction.InventoryMovementTypeRepository;
import com.example.orderprocessingservice.service.domain.TransactionService;
import com.example.orderprocessingservice.utils.InventoryMovementReferenceType;
import com.example.orderprocessingservice.validator.StockValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.Year;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    // Repository dependencies
    private final StockRepository stockRepository;
    private final WareHouseRepository wareHouseRepository;
    private final WareHouseInventoryBacklogRepository wareHouseInventoryBacklogRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryMovementTypeRepository inventoryMovementTypeRepository;
    private final EmployeeRepository employeeRepository;

    // Service dependencies
    private final TransactionService transactionService;

    // Validator and Mapper dependencies
    private final StockValidator stockValidator;
    private final StockMapper stockMapper;
    private final CustomerOrderMapper customerOrderMapper;

    @Transactional
    public void handleNewStock(StockMP stockRequest) {
        LOGGER.info("Processing new stock: {}", stockRequest);
        stockValidator.validate(stockRequest);
        // Step 1: Validating warehouse and capacity
        StockContext context = buildStockContext(stockRequest);
        validateWarehouseCapacity(context);
        // Step 2: Check if stock already exists and updating if needed
        if (updateExistingStockIfPresent(stockRequest, context.getWareHouse())) {
            return;
        }
        // Step 3: Creating new stock entity
        Stock newStock = stockMapper.toEntity(stockRequest);
        // Step 4: Processing warehouse inventory backlog (debt fulfillment)
        BacklogProcessingResult backlogResult = processWarehouseBacklog(newStock);
        // Step 5: Saving remaining stock if any
        saveRemainingStock(newStock, backlogResult.getRemainingStock(), context.getWareHouse(), context.getNewTotalCapacity());
    }

    @Transactional
    public void handleDeleteStock(String id) {
        LOGGER.info("Processing Stock deletion for ID: {}", id);
        try {
            int stockId = Integer.parseInt(id);

            StockDeletionContext deletionContext = buildStockDeletionContext(stockId);
            validateStockDeletion(deletionContext);
            executeStockDeletion(deletionContext);

            LOGGER.info("Successfully deleted Stock with ID: {} and updated warehouse capacity to {}",
                    id, deletionContext.getNewCapacity());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Stock ID format: " + id);
        }
    }

    public void createSupplyOrderForInventoryMovement(WareHouse wareHouse, Product product, Supply supply, SupplyTransaction supplyTransaction,Employee employee) {
        InventoryMovement inventoryMovement = new InventoryMovement();
        inventoryMovement.setWareHouse(wareHouse);
        inventoryMovement.setProduct(product);
        inventoryMovement.setInventoryMovementType(inventoryMovementTypeRepository.findByInventoryMovementTypeId(
                InventoryMovementReferenceType.RECEIVE.getType_id()));
        inventoryMovement.setQuantity(supply.getAmount());
        inventoryMovement.setUnitCost(product.getProduct_price());
        String batchNumber = String.format(
                "BATCH-%d-%04d",
                Year.now().getValue(),
                new SecureRandom().nextInt(10_000)
        );
        inventoryMovement.setBatch_number(batchNumber);
        inventoryMovement.setExpiry_date(supplyTransaction.getExpected_delivery_time().toLocalDateTime());
        inventoryMovement.setReference_type(InventoryMovementReferenceType.PURCHASE_ORDER.getValue());
        inventoryMovement.setReference_id(supply.getId());
        inventoryMovement.setEmployee(employee);
        inventoryMovement.setNotes(String.format("Purchased product %s with quantity %s",product.getProduct_name(),supply.getAmount()));

        inventoryMovementRepository.save(inventoryMovement);
    }

    // Helper Methods ---------------------------------------------------------------------------------------

    private StockContext buildStockContext(StockMP stockRequest) {
        Optional<WareHouse> wareHouseOpt = wareHouseRepository.findById(stockRequest.getWare_house_id());
        if (wareHouseOpt.isEmpty()) {
            throw WareHouseException.notFound(stockRequest.getWare_house_id());
        }
        WareHouse wareHouse = wareHouseOpt.get();
        LOGGER.info("WareHouse: warHouseCapacity: {}, wareHouseName: {}", wareHouse.getWareHouseCapacity(),wareHouse.getWareHouseName());
        int newTotalCapacity = wareHouse.getWareHouseCapacity() + stockRequest.getQuantity();
        LOGGER.info("Warehouse capacity to {} is {}", newTotalCapacity, wareHouse.getWareHouseCapacity());
        return StockContext.builder()
                .wareHouse(wareHouse)
                .newTotalCapacity(newTotalCapacity)
                .requestedQuantity(stockRequest.getQuantity())
                .productId(stockRequest.getProduct_id())
                .build();
    }

    private void validateWarehouseCapacity(StockContext context) {
        if (context.getNewTotalCapacity() > context.getWareHouse().getMaxStockLevel()) {
            throw StockException.capacityExceeded(
                    context.getWareHouse().getWareHouseId(),
                    context.getNewTotalCapacity(),
                    context.getWareHouse().getWareHouseCapacity()
            );
        }
    }

    private boolean updateExistingStockIfPresent(StockMP stockRequest, WareHouse wareHouse) {
        Stock existingStock = stockRepository.findByProductIdAndWareHouseId(
                stockRequest.getProduct_id(), stockRequest.getWare_house_id());

        if (existingStock != null) {
            LOGGER.warn("Stock already exists for product ID {} and warehouse ID {}",
                    stockRequest.getProduct_id(), stockRequest.getWare_house_id());

            existingStock.setQuantity(existingStock.getQuantity() + stockRequest.getQuantity());
            stockRepository.save(existingStock);

            int newCapacity = wareHouse.getWareHouseCapacity() + stockRequest.getQuantity();
            wareHouse.setWareHouseCapacity(newCapacity);
            wareHouseRepository.save(wareHouse);

            LOGGER.info("Successfully updated existing stock with product ID: {} and updated quantity to {}",
                    stockRequest.getProduct_id(), existingStock.getQuantity());
            LOGGER.info("Successfully updated warehouse capacity to {}", newCapacity);
            return true;
        }
        return false;
    }

    private BacklogProcessingResult processWarehouseBacklog(Stock newStock) {
        int wareHouseId = newStock.getWareHouse().getWareHouseId();
        String productId = newStock.getProduct().getProduct_id();
        int availableStock = newStock.getQuantity();

        List<WareHouseInventoryBacklog> backlogList = wareHouseInventoryBacklogRepository
                .findAllByWareHouseId_ProductId(wareHouseId, productId);

        if (backlogList.isEmpty()) {
            LOGGER.debug("No backlog found for product {} in warehouse {}", productId, wareHouseId);
            return BacklogProcessingResult.builder()
                    .remainingStock(availableStock)
                    .processedBacklogs(new ArrayList<>())
                    .build();
        }

        // Sorting by debt quantity descending (processing largest debts first)
        backlogList.sort((o1, o2) -> o2.getDebtQuantity().compareTo(o1.getDebtQuantity()));

        List<BacklogProcessingInfo> processedBacklogs = new ArrayList<>();

        for (WareHouseInventoryBacklog backlog : backlogList) {
            if (availableStock <= 0) {
                break;
            }

            BacklogProcessingInfo processingInfo = processSingleBacklog(backlog, availableStock, newStock.getWareHouse());
            processedBacklogs.add(processingInfo);
            availableStock -= processingInfo.getFulfilledQuantity();
        }

        return BacklogProcessingResult.builder()
                .remainingStock(availableStock)
                .processedBacklogs(processedBacklogs)
                .build();
    }

    private BacklogProcessingInfo processSingleBacklog(WareHouseInventoryBacklog backlog, int availableStock, WareHouse wareHouse) {
        int debtQuantity = backlog.getDebtQuantity();
        int fulfilledQuantity = Math.min(availableStock, debtQuantity);

        BacklogProcessingInfo processingInfo = BacklogProcessingInfo.builder()
                .backlog(backlog)
                .originalDebt(debtQuantity)
                .fulfilledQuantity(fulfilledQuantity)
                .build();

        if (availableStock >= debtQuantity) {
            // Fully satisfying the debt
            wareHouseInventoryBacklogRepository.delete(backlog);
            LOGGER.info("Successfully deleted WareHouseInventoryBacklog with ID: {}", backlog.getId());
        } else {
            // Partially satisfying the debt
            int remainingDebt = debtQuantity - availableStock;
            backlog.setDebtQuantity(remainingDebt);
            wareHouseInventoryBacklogRepository.save(backlog);
            LOGGER.info("Successfully updated debt quantity for WareHouseInventoryBacklog with ID: {} to {}",
                    backlog.getId(), remainingDebt);
        }

        // Creating a new customer order for fulfilled quantity
        createCustomerOrderForBacklog(backlog, fulfilledQuantity, wareHouse);

        return processingInfo;
    }

    private void createCustomerOrderForBacklog(WareHouseInventoryBacklog backlog, int fulfilledQuantity, WareHouse wareHouse) {
        try {
            int customerId = backlog.getCustomerOrder().getCustomer().getCustomer_id();
            String productId = backlog.getProduct().getProduct_id();

            // Creating a new customer order for the fulfilled amount
            CustomerOrderMP customerOrderMP = new CustomerOrderMP();
            customerOrderMP.setCustomer_id(customerId);
            customerOrderMP.setProduct_id(productId);
            customerOrderMP.setOrder_time(OffsetDateTime.now());
            customerOrderMP.setProduct_amount(fulfilledQuantity);

            CustomerOrder newCustomerOrder = customerOrderMapper.toEntity(customerOrderMP);
            customerOrderRepository.save(newCustomerOrder);
            LOGGER.info("Successfully saved new customer order for backlog fulfillment: {}", newCustomerOrder.getOrderId());

            // Creating customer transaction
            Customer customer = getCustomerById(customerId);
            Collection<WareHouse> wareHouses = Collections.singleton(wareHouse);
            CustomerTransaction customerTransaction = transactionService.create(newCustomerOrder, customer, wareHouses);
            LOGGER.info("Created customer transaction with expected delivery time: {}",
                    customerTransaction.getExpected_delivery_time());

            // Recording inventory movement for debt payment
            // TODO We need to call this if we have a debt
            recordDebtPaymentInventoryMovement(customer, backlog.getProduct(), wareHouse, fulfilledQuantity);

        } catch (Exception e) {
            LOGGER.error("Failed to create customer order for backlog: {}", e.getMessage());
            throw new RuntimeException("Failed to create customer order for backlog", e);
        }
    }

    private Customer getCustomerById(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw CustomerException.notFound(customerId);
        }
        return customer.get();
    }

    private void recordDebtPaymentInventoryMovement(Customer customer, Product product, WareHouse wareHouse, int quantity) {
        try {
            List<Employee> employees = employeeRepository.findAllEmployees();
            Employee randomEmployee = employees.get(new Random().nextInt(employees.size()));

            InventoryMovement movement = InventoryMovement.builder()
                    .wareHouse(wareHouse)
                    .product(product)
                    .inventoryMovementType(inventoryMovementTypeRepository.findByInventoryMovementTypeId(
                            InventoryMovementReferenceType.DEBT_PAYED.getType_id()))
                    .customer(customer)
                    .quantity(quantity)
                    .unitCost(product.getProduct_price())
                    .reference_type(InventoryMovementReferenceType.CUSTOMER_ORDER_DEBT_PAYED.getValue())
                    .employee(randomEmployee)
                    .notes(String.format("Customer Order debt paid - Customer ID: %d, Quantity: %d, Product: %s",
                            customer.getCustomer_id(), quantity, product.getProduct_id()))
                    .build();

            inventoryMovementRepository.save(movement);
            LOGGER.info("Successfully recorded debt payment inventory movement with ID: {}", movement.getId());
        } catch (Exception e) {
            LOGGER.error("Failed to record debt payment inventory movement: {}", e.getMessage());
            throw new RuntimeException("Failed to record debt payment inventory movement", e);
        }
    }

    private void saveRemainingStock(Stock newStock, int remainingQuantity, WareHouse wareHouse, int newTotalCapacity) {
        if (remainingQuantity > 0) {
            newStock.setQuantity(remainingQuantity);
            stockRepository.save(newStock);

            wareHouse.setWareHouseCapacity(newTotalCapacity);
            wareHouseRepository.save(wareHouse);

            LOGGER.info("Successfully saved new stock with product ID: {} with remaining quantity: {} and updated warehouse capacity to {}",
                    newStock.getProduct().getProduct_id(), remainingQuantity, newTotalCapacity);
        } else {
            LOGGER.info("All stock was used to fulfill backlog for product ID: {}", newStock.getProduct().getProduct_id());
        }
    }

    private StockDeletionContext buildStockDeletionContext(int stockId) {
        Optional<Stock> stock = stockRepository.findById(stockId);
        if (stock.isEmpty()) {
            throw StockException.notFound(String.valueOf(stockId));
        }

        Stock stockEntity = stock.get();
        int wareHouseId = stockEntity.getWareHouse().getWareHouseId();

        Optional<WareHouse> wareHouse = wareHouseRepository.findById(wareHouseId);
        if (wareHouse.isEmpty()) {
            throw WareHouseException.notFound(wareHouseId);
        }

        WareHouse wareHouseEntity = wareHouse.get();
        int newCapacity = wareHouseEntity.getWareHouseCapacity() - stockEntity.getQuantity();

        return StockDeletionContext.builder()
                .stock(stockEntity)
                .wareHouse(wareHouseEntity)
                .newCapacity(newCapacity)
                .stockId(stockId)
                .build();
    }

    private void validateStockDeletion(StockDeletionContext context) {
        if (context.getNewCapacity() < context.getWareHouse().getMinStockLevel()) {
            LOGGER.warn("Deleting stock will bring warehouse {} below minimum stock level ({} < {})",
                    context.getWareHouse().getWareHouseId(),
                    context.getNewCapacity(),
                    context.getWareHouse().getMinStockLevel());
        }
    }

    private void executeStockDeletion(StockDeletionContext context) {
        context.getWareHouse().setWareHouseCapacity(context.getNewCapacity());
        wareHouseRepository.save(context.getWareHouse());
        stockRepository.delete(context.getStock());
    }

    @lombok.Builder
    @lombok.Data
    private static class StockContext {
        private WareHouse wareHouse;
        private int newTotalCapacity;
        private int requestedQuantity;
        private String productId;
    }

    @lombok.Builder
    @lombok.Data
    private static class BacklogProcessingResult {
        private int remainingStock;
        private List<BacklogProcessingInfo> processedBacklogs;
    }

    @lombok.Builder
    @lombok.Data
    private static class BacklogProcessingInfo {
        private WareHouseInventoryBacklog backlog;
        private int originalDebt;
        private int fulfilledQuantity;
    }

    @lombok.Builder
    @lombok.Data
    private static class StockDeletionContext {
        private Stock stock;
        private WareHouse wareHouse;
        private int newCapacity;
        private int stockId;
    }

}