package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.asset.Supply;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.transaction.SupplyTransaction;
import com.example.orderprocessingservice.exception.asset.SupplyException;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.asset.SupplyRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.transaction.SupplyTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplyOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplyOrderService.class);
    private final SupplyRepository supplyRepository;
    private final SupplyTransactionRepository supplyTransactionRepository;
    private final WareHouseRepository wareHouseRepository;
    private final StockRepository stockRepository;


    // TODO: We need to update logic here to include method handleNewStock from StockService here
    public void speedUpEmployeeSupply(int supplyId) {
        LOGGER.info("Speeding up employee supply with ID: {}", supplyId);
        Optional<Supply> supply = supplyRepository.findById(supplyId);
        if (supply.isEmpty()) {
            throw SupplyException.notFound(supplyId);
        }
        SupplyTransaction supplyTransaction = supplyTransactionRepository.findBySupply_Id(supplyId);
        if (supplyTransaction.isDelivered()) {
            LOGGER.info("Customer order with ID {} is already finished", supplyId);
            return;
        }
        supplyTransaction.setDelivered(true);
        supplyTransactionRepository.save(supplyTransaction);
        LOGGER.info("Successfully finished speedUp of employee supply with ID {}", supplyId);

        Employee employee = supply.get().getEmployee();
        Supply supplyEntity = supply.get();
        int wareHouseId = employee.getWareHouse().getWareHouseId();
        Optional<WareHouse> wareHouseEntity = wareHouseRepository.findById(wareHouseId);
        if (wareHouseEntity.isEmpty()) {
            throw SupplyException.notFound(supplyId);
        }
        WareHouse wareHouse = wareHouseEntity.get();

        LOGGER.debug("Updating warehouse capacity...");
        wareHouse.setWareHouseCapacity(wareHouse.getWareHouseCapacity() + supplyEntity.getAmount());
        wareHouseRepository.save(wareHouse);
        LOGGER.info("Updated warehouse capacity to: {}", wareHouse.getWareHouseCapacity());

        Product product = supplyEntity.getProduct();
        LOGGER.debug("Checking existing stock for product ID: {}", product.getProduct_id());
        Stock stock = stockRepository.findByProductIdAndWareHouseId(product.getProduct_id(),wareHouseId);
        if (stock == null) {
            LOGGER.debug("No existing stock found, creating new stock entry");
            Stock newStock = Stock.builder()
                    .wareHouse(wareHouse)
                    .product(product)
                    .quantity(supplyEntity.getAmount())
                    .build();
            stockRepository.save(newStock);
            LOGGER.info("Successfully saved new stock for product ID: {}", product.getProduct_id());
        } else {
            stock.setQuantity(stock.getQuantity() + supplyEntity.getAmount());
            stockRepository.save(stock);
            LOGGER.info("Successfully updated stock for product ID: {}", product.getProduct_id());
        }
    }
}
