package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.SupplyMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Supply;
import com.example.orderprocessingservice.dto.model.order.RouteCalculationResponse;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.dto.model.transaction.SupplyTransaction;
import com.example.orderprocessingservice.exception.asset.ProductException;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.exception.personnel.EmployeeException;
import com.example.orderprocessingservice.exception.personnel.WareHouseException;
import com.example.orderprocessingservice.exception.supplier.SupplierException;
import com.example.orderprocessingservice.mapper.supply.SupplyMapper;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.asset.SupplyRepository;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import com.example.orderprocessingservice.repository.transaction.SupplyTransactionRepository;
import com.example.orderprocessingservice.validator.SupplyValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplyService.class);
    private final SupplyRepository supplyRepository;
    private final SupplyValidator supplyValidator;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final SupplyMapper supplyMapper;
    private final WareHouseRepository wareHouseRepository;
    private final RouteCalculationService routeCalculationService;
    private final SupplyTransactionRepository supplyTransactionRepository;

    @Transactional
    public void handleNewSupply(SupplyMP supply) {
        LOGGER.info("Processing new supply: {}", supply);

        try {
            supplyValidator.validate(supply);

            LOGGER.debug("Fetching supplier with ID: {}", supply.getSupplier_id());
            Optional<Supplier> supplier = supplierRepository.findById(supply.getSupplier_id());
            if (supplier.isEmpty()) {
                LOGGER.error("Supplier not found with ID: {}", supply.getSupplier_id());
                throw SupplierException.notFound(supply.getSupplier_id());
            }
            LOGGER.debug("Supplier found successfully");

            LOGGER.debug("Fetching product with ID: {}", supply.getProduct_id());
            Product product = productRepository.findByProductId(supply.getProduct_id());
            if (product == null) {
                LOGGER.error("Product not found with ID: {}", supply.getProduct_id());
                throw ProductException.notFound(supply.getProduct_id());
            }
            LOGGER.debug("Product found successfully");

            LOGGER.debug("Fetching employee with ID: {}", supply.getEmployee_id());
            Optional<Employee> employee = employeeRepository.findById(supply.getEmployee_id());
            if (employee.isEmpty()) {
                LOGGER.error("Employee not found with ID: {}", supply.getEmployee_id());
                throw EmployeeException.notFound(supply.getEmployee_id());
            }

            Employee employeeEntity = employee.get();
            LOGGER.debug("Employee found successfully");

            int wareHouseId = employeeEntity.getWareHouse().getWareHouseId();
            LOGGER.debug("Fetching warehouse for employee with warehouse ID: {}", wareHouseId);
            Optional<WareHouse> wareHouse = wareHouseRepository.findById(wareHouseId);
            if (wareHouse.isEmpty()) {
                LOGGER.error("Warehouse not found with ID: {}", wareHouseId);
                throw EmployeeException.notFound(wareHouseId);
            }
            WareHouse wareHouseEntity = wareHouse.get();
            LOGGER.debug("Warehouse found successfully");

            LOGGER.debug("Checking warehouse capacity. Current: {}, Adding: {}, Max: {}",
                    wareHouseEntity.getWareHouseCapacity(),
                    supply.getAmount(),
                    wareHouseEntity.getMaxStockLevel());
            if (wareHouseEntity.getWareHouseCapacity() + supply.getAmount() > wareHouseEntity.getMaxStockLevel()) {
                LOGGER.error("Warehouse capacity would be exceeded. Current: {}, Adding: {}, Max: {}",
                        wareHouseEntity.getWareHouseCapacity(),
                        supply.getAmount(),
                        wareHouseEntity.getMaxStockLevel());
                throw WareHouseException.capacityExceeded(wareHouseEntity.getWareHouseId(), supply.getAmount(), wareHouseEntity.getWareHouseCapacity());
            }

            Supply newSupply = supplyMapper.toEntity(supply);

            LOGGER.info("New Supply details: [ID: {}, Supplier: {}, Product: {}, Employee: {}, Supply Time: {}, Amount: {}]",
                    newSupply.getId(),
                    newSupply.getSupplier() != null ? newSupply.getSupplier().getSupplier_id() : "null",
                    newSupply.getProduct() != null ? newSupply.getProduct().getProduct_id() : "null",
                    newSupply.getEmployee() != null ? newSupply.getEmployee().getEmployee_id() : "null",
                    newSupply.getSupplyTime(),
                    newSupply.getAmount()
            );

            LOGGER.debug("Saving new supply record...");
            supplyRepository.save(newSupply);
            LOGGER.info("Successfully saved new supply: {}", newSupply);

            LOGGER.debug("Calculating delivery route...");
            Supplier supplierEntity = supplier.get();
            RouteCalculationResponse route = routeCalculationService.calculateRoute(
                    supplierEntity.getLatitude(),
                    supplierEntity.getLongitude(),
                    wareHouseEntity.getLatitude(),
                    wareHouseEntity.getLongitude()
            );
            LOGGER.debug("Route calculated successfully: {}", route);

            LOGGER.debug("Creating supply transaction...");
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime expectedDeliveryTime = now.plus(Duration.ofSeconds((long) route.getDurationSeconds()));
            SupplyTransaction supplyTransaction = SupplyTransaction.builder()
                    .supply(newSupply)
                    .expected_delivery_time(expectedDeliveryTime)
                    .delivered(false)
                    .build();
            supplyTransactionRepository.save(supplyTransaction);

            LOGGER.info("Successfully created supply transaction with expected delivery time: {}",
                    expectedDeliveryTime);
        } catch (Exception e) {
            LOGGER.error("Failed to process supply operation: {}", e.getMessage());
            throw new RuntimeException("Failed to process supply operation", e); // Re-throw the exception
        }
    }

    @Transactional
    public void handleDeleteSupply(String id) {
        LOGGER.info("Processing supply deletion for ID: {}", id);
        try {
            int supplyId = Integer.parseInt(id);
            if (!supplyRepository.existsById(supplyId)) {
                throw CustomerException.notFound(supplyId);
            }
            supplyRepository.deleteById(supplyId);
            LOGGER.info("Successfully deleted supply with ID: {}", id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid supply ID format: " + id);
        }
    }
}
