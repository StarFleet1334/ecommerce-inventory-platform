package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.StockMP;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.mapper.stock.StockMapper;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.validator.StockValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    private final StockRepository stockRepository;
    private final StockValidator stockValidator;
    private final WareHouseRepository wareHouseRepository;
    private final StockMapper stockMapper;

    @Transactional
    public void handleNewStock(StockMP stock) {
        LOGGER.info("Processing new stock: {}", stock);

        stockValidator.validate(stock);

        WareHouse wareHouse = wareHouseRepository.findById(stock.getWare_house_id()).get();

        int newTotalStock = wareHouse.getWareHouseCapacity() + stock.getQuantity();

        if (newTotalStock > wareHouse.getMaxStockLevel()) {
            throw new IllegalStateException(
                    String.format("Adding %d items would exceed warehouse maximum capacity of %d",
                            stock.getQuantity(), wareHouse.getMaxStockLevel())
            );
        }

        Stock newStock = stockMapper.toEntity(stock);

        try {
            stockRepository.save(newStock);
            wareHouse.setWareHouseCapacity(newTotalStock);
            wareHouseRepository.save(wareHouse);
            LOGGER.info("Successfully saved new stock with product ID: {} and updated warehouse capacity",
                    stock.getProduct_id());
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

            Stock stock = stockRepository.findById(stockId)
                    .orElseThrow(() -> new IllegalArgumentException("Stock with ID " + id + " does not exist"));

            WareHouse wareHouse = wareHouseRepository.findById(stock.getWareHouse().getWareHouseId())
                    .orElseThrow(() -> new IllegalArgumentException("Warehouse not found for stock ID: " + id));

            int newCapacity = wareHouse.getWareHouseCapacity() - stock.getQuantity();
            if (newCapacity < wareHouse.getMinStockLevel()) {
                LOGGER.warn("Deleting stock will bring warehouse {} below minimum stock level ({} < {})",
                        wareHouse.getWareHouseId(), newCapacity, wareHouse.getMinStockLevel());
            }

            wareHouse.setWareHouseCapacity(newCapacity);
            wareHouseRepository.save(wareHouse);
            stockRepository.delete(stock);

            LOGGER.info("Successfully deleted Stock with ID: {} and updated warehouse capacity to {}",
                    id, newCapacity);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Stock ID format: " + id);
        }
    }

}
