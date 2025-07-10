package com.example.orderprocessingservice.config.database.loaders;

import com.example.orderprocessingservice.config.database.AbstractMapDataLoader;
import com.example.orderprocessingservice.dto.messages.StockMessage;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.utils.constants.UrlConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public abstract class StockLoader extends AbstractMapDataLoader<Stock, StockMessage> {
    private final WareHouseRepository wareHouseRepository;
    private final ProductRepository productRepository;
    private Map<Integer, Integer> warehouseTotals;

    @Autowired
    public StockLoader(ObjectMapper objectMapper,
                       StockRepository repository,
                       WareHouseRepository wareHouseRepository,
                       ProductRepository productRepository) {
        super(objectMapper,
                "data/stock.json",
                UrlConstants.STOCK_POST_ENDPOINT,
                repository);
        this.wareHouseRepository = wareHouseRepository;
        this.productRepository = productRepository;
        this.warehouseTotals = new HashMap<>();
    }

    @Override
    protected TypeReference<List<Map<String, Object>>> getMapTypeReference() {
        return new TypeReference<>() {
        };
    }

    @Override
    protected List<Stock> processEntries(List<Map<String, Object>> entries) {
        List<Stock> validStocks = new ArrayList<>();
        warehouseTotals.clear();

        for (Map<String, Object> entry : entries) {
            processStockEntry(entry).ifPresent(validStocks::add);
        }

        return validStocks;
    }

    @Override
    protected void postProcessEntities(List<Stock> entities) {
        updateWarehouseCapacities();
    }

    private Optional<Stock> processStockEntry(Map<String, Object> entry) {
        Integer wareHouseId = (Integer) entry.get("ware_house_id");
        String productId = (String) entry.get("product_id");
        Integer quantity = (Integer) entry.get("quantity");

        if (wareHouseId == null || productId == null || quantity == null) {
            System.out.println("⚠️ Skipping entry due to missing fields: " + entry);
            return Optional.empty();
        }

        try {
            WareHouse wareHouse = wareHouseRepository.findById(wareHouseId)
                    .orElseThrow(() -> new RuntimeException("WareHouse not found with id: " + wareHouseId));

            Product product = productRepository.findByProductId(productId);
            if (product == null) {
                System.out.println("Product is not found: " + productId);
                return Optional.empty();
            }

            if (!isStockLevelValid(wareHouse, wareHouseId, quantity)) {
                return Optional.empty();
            }

            Stock stock = Stock.builder()
                    .product(product)
                    .wareHouse(wareHouse)
                    .quantity(quantity)
                    .build();

            updateWarehouseTotals(wareHouseId, wareHouse, quantity);
            return Optional.of(stock);

        } catch (Exception e) {
            System.out.println("Error processing stock entry: " + e.getMessage());
            return Optional.empty();
        }
    }

    private boolean isStockLevelValid(WareHouse wareHouse, Integer wareHouseId, Integer quantity) {
        int currentTotal = warehouseTotals.getOrDefault(wareHouseId, 0);
        int newTotal = currentTotal + quantity;

        if (newTotal > wareHouse.getMaxStockLevel()) {
            System.out.println("❌ Skipped: adding " + quantity + " to " + wareHouse.getWareHouseName() +
                    " exceeds max_stock_level (" + newTotal + "/" + wareHouse.getMaxStockLevel() + ")");
            return false;
        }
        return true;
    }

    private void updateWarehouseTotals(Integer wareHouseId, WareHouse wareHouse, Integer quantity) {
        int currentTotal = warehouseTotals.getOrDefault(wareHouseId, 0);
        int newTotal = currentTotal + quantity;
        wareHouse.setWareHouseCapacity(newTotal);
        warehouseTotals.put(wareHouseId, newTotal);
    }

    private void updateWarehouseCapacities() {
        Set<Integer> updatedWarehouses = warehouseTotals.keySet();
        List<WareHouse> warehousesToUpdate = wareHouseRepository.findAllById(updatedWarehouses);
        for (WareHouse warehouse : warehousesToUpdate) {
            warehouse.setWareHouseCapacity(warehouseTotals.get(warehouse.getWareHouseId()));
        }
        wareHouseRepository.saveAll(warehousesToUpdate);
    }

    @Override
    protected StockMessage convertToMessage(Stock stock) {
        StockMessage stockMessage = new StockMessage();
        stockMessage.setProduct_id(stock.getProduct().getProduct_id());
        stockMessage.setWare_house_id(stock.getWareHouse().getWareHouseId());
        stockMessage.setQuantity(stock.getQuantity());
        return stockMessage;
    }

    @Override
    protected String getEntityIdentifier(Stock entity) {
        return entity.getProduct().getProduct_id();
    }
}
