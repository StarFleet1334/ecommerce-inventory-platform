package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.StockMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
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
    private final StockService stockService;

    // TODO: We need to update logic here to include method handleNewStock from StockService here ( TO BE TESTED ) !!!
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
        StockMP stockMP = new StockMP();
        stockMP.setProduct_id(product.getProduct_id());
        stockMP.setWare_house_id(wareHouseId);
        stockMP.setQuantity(supplyEntity.getAmount());

        LOGGER.info("Using StockService.handleNewStock for product ID: {} with quantity: {}",
                product.getProduct_id(), supplyEntity.getAmount());

        stockService.handleNewStock(stockMP);

        LOGGER.info("Successfully processed supply using handleNewStock for supply ID: {}", supplyId);
    }
}
