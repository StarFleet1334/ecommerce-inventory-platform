package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.StockMP;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.exception.asset.StockException;
import com.example.orderprocessingservice.exception.personnel.WareHouseException;
import com.example.orderprocessingservice.mapper.stock.StockMapper;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.validator.StockValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
            throw StockException.capacityExceeded(wareHouse.getWareHouseId(),newTotalStock,wareHouse.getWareHouseCapacity());
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
