package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
import com.example.orderprocessingservice.dto.eventDto.StockMP;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.dto.model.order.RouteCalculationResponse;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.personnel.WareHouseInventoryBacklog;
import com.example.orderprocessingservice.dto.model.transaction.CustomerTransaction;
import com.example.orderprocessingservice.exception.asset.StockException;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.exception.personnel.WareHouseException;
import com.example.orderprocessingservice.mapper.customer.CustomerOrderMapper;
import com.example.orderprocessingservice.mapper.stock.StockMapper;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.customer.CustomerOrderRepository;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseInventoryBacklogRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.transaction.CustomerTransactionRepository;
import com.example.orderprocessingservice.validator.StockValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    private final StockRepository stockRepository;
    private final StockValidator stockValidator;
    private final WareHouseRepository wareHouseRepository;
    private final StockMapper stockMapper;
    private final WareHouseInventoryBacklogRepository wareHouseInventoryBacklogRepository;
    private final CustomerOrderMapper customerOrderMapper;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;
    private final CustomerTransactionRepository customerTransactionRepository;
    private final RouteCalculationService routeCalculationService;

    @Transactional
    public void handleNewStock(StockMP stock) {
        LOGGER.info("Processing new stock: {}", stock);

        stockValidator.validate(stock);

        WareHouse wareHouse = wareHouseRepository.findById(stock.getWare_house_id()).get();

        int newTotalStock = wareHouse.getWareHouseCapacity() + stock.getQuantity();

        if (newTotalStock > wareHouse.getMaxStockLevel()) {
            throw StockException.capacityExceeded(wareHouse.getWareHouseId(), newTotalStock, wareHouse.getWareHouseCapacity());
        }

        Stock newStock = stockMapper.toEntity(stock);

        try {
            // STAGE 3 of WareHouse Logic
            int wareHouseId = newStock.getWareHouse().getWareHouseId();
            String productId = newStock.getProduct().getProduct_id();
            int amount_in_stock = newStock.getQuantity();

            List<WareHouseInventoryBacklog> wareHouseInventoryBacklogList = wareHouseInventoryBacklogRepository.findAllByWareHouseId_ProductId(wareHouseId, productId);
            wareHouseInventoryBacklogList.sort((o1, o2) -> o2.getDebtQuantity().compareTo(o1.getDebtQuantity()));
            if (!wareHouseInventoryBacklogList.isEmpty()) {
                for (WareHouseInventoryBacklog wareHouseInventoryBacklog : wareHouseInventoryBacklogList) {
                    int debtQuantity = wareHouseInventoryBacklog.getDebtQuantity();
                    // STAGE 4 of WareHouse Logic
                    if (amount_in_stock >= debtQuantity) {
                        wareHouseInventoryBacklogRepository.delete(wareHouseInventoryBacklog);
                        amount_in_stock -= debtQuantity;
                        LOGGER.info("Successfully deleted WareHouseInventoryBacklog with ID: {}", wareHouseInventoryBacklog.getId());
                    } else {
                        int remaining_debt_quantity = debtQuantity - amount_in_stock;
                        wareHouseInventoryBacklog.setDebtQuantity(remaining_debt_quantity);
                        wareHouseInventoryBacklogRepository.save(wareHouseInventoryBacklog);
                        amount_in_stock = 0;
                        LOGGER.info("Successfully updated debt quantity for WareHouseInventoryBacklog with ID: {}", wareHouseInventoryBacklog.getId());
                    }
                    int customerId = wareHouseInventoryBacklog.getCustomerOrder().getCustomer().getCustomer_id();
                    CustomerOrderMP customerOrderMP = new CustomerOrderMP();
                    customerOrderMP.setCustomer_id(customerId);
                    customerOrderMP.setProduct_id(productId);
                    customerOrderMP.setOrder_time(OffsetDateTime.now());
                    customerOrderMP.setProduct_amount(debtQuantity);
                    CustomerOrder newCustomerOrder = customerOrderMapper.toEntity(customerOrderMP);
                    customerOrderRepository.save(newCustomerOrder);
                    LOGGER.info("Successfully saved new customer order: {}", newCustomerOrder);

                    // STAGE 5 of WareHouse Logic

                    Optional<Customer> customer = customerRepository.findById(customerId);
                    if (customer.isEmpty()) {
                        throw CustomerException.notFound(customerId);
                    }

                    WareHouse ware_house = newStock.getWareHouse();

                    RouteCalculationResponse bestRoute = null;
                    BigDecimal customerLat = customer.get().getLatitude();
                    BigDecimal customerLon = customer.get().getLongitude();

                    RouteCalculationResponse route = routeCalculationService.calculateRoute(
                            customerLat, customerLon, ware_house.getLatitude(), ware_house.getLongitude());

                    if (bestRoute == null || route.getDistanceKm() < bestRoute.getDistanceKm()) {
                        bestRoute = route;
                    }
                    LOGGER.info("Best route for customer order {} is {}", customerOrderMP, bestRoute);
                    CustomerTransaction customerTransaction = new CustomerTransaction();
                    customerTransaction.setCustomerOrder(newCustomerOrder);

                    if (bestRoute != null) {
                        OffsetDateTime now = OffsetDateTime.now();
                        OffsetDateTime expectedDeliveryTime = now.plus(Duration.ofSeconds((long) bestRoute.getDurationSeconds()));
                        customerTransaction.setExpected_delivery_time(expectedDeliveryTime);
                    } else {
                        customerTransaction.setExpected_delivery_time(OffsetDateTime.now().plusHours(24));
                        LOGGER.warn("No route found, setting default delivery time for order: {}", customerOrderMP);
                    }

                    customerTransaction.setFinished(false);

                    customerTransactionRepository.save(customerTransaction);
                    LOGGER.info("Created customer transaction with expected delivery time: {}",
                            customerTransaction.getExpected_delivery_time());

                }
                newStock.setQuantity(amount_in_stock);
            }

            if (amount_in_stock > 0) {
                stockRepository.save(newStock);
                wareHouse.setWareHouseCapacity(newTotalStock);
                wareHouseRepository.save(wareHouse);
                LOGGER.info("Successfully saved new stock with product ID: {} and updated warehouse capacity",
                        stock.getProduct_id());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to process stock operation: {}", e.getMessage());
            throw new RuntimeException("Failed to process stock operation", e);
        }
    }

    @Transactional
    public void handleDeleteStock(String id) {
        LOGGER.info("Processing Stock deletion for ID: {}", id);
        try {
            int stockId = Integer.parseInt(id);

            Optional<Stock> stock = stockRepository.findById(stockId);
            if (stock.isEmpty()) {
                throw StockException.notFound(id);
            }

            int wareHouseId = stock.get().getWareHouse().getWareHouseId();
            Optional<WareHouse> wareHouse = wareHouseRepository.findById(wareHouseId);
            if (wareHouse.isEmpty()) {
                throw WareHouseException.notFound(wareHouseId);
            }
            WareHouse wareHouseEntity = wareHouse.get();

            int newCapacity = wareHouseEntity.getWareHouseCapacity() - stock.get().getQuantity();
            if (newCapacity < wareHouseEntity.getMinStockLevel()) {
                LOGGER.warn("Deleting stock will bring warehouse {} below minimum stock level ({} < {})",
                        wareHouseEntity.getWareHouseId(), newCapacity, wareHouseEntity.getMinStockLevel());
            }

            wareHouseEntity.setWareHouseCapacity(newCapacity);
            wareHouseRepository.save(wareHouseEntity);
            stockRepository.delete(stock.get());

            LOGGER.info("Successfully deleted Stock with ID: {} and updated warehouse capacity to {}",
                    id, newCapacity);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Stock ID format: " + id);
        }
    }

}
