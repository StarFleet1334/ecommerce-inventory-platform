package com.example.orderprocessingservice.service;

import com.example.orderprocessingservice.dto.eventDto.WareHouseMP;
import com.example.orderprocessingservice.dto.messages.WhTransferMessage;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.transaction.InventoryMovement;
import com.example.orderprocessingservice.exception.asset.ProductException;
import com.example.orderprocessingservice.exception.personnel.WareHouseException;
import com.example.orderprocessingservice.mapper.warehouse.WarehouseMapper;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.transaction.InventoryMovementRepository;
import com.example.orderprocessingservice.repository.transaction.InventoryMovementTypeRepository;
import com.example.orderprocessingservice.utils.InventoryMovementReferenceType;
import com.example.orderprocessingservice.validator.WareHouseValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WareHouseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WareHouseService.class);
    private final WareHouseRepository wareHouseRepository;
    private final WareHouseValidator wareHouseValidator;
    private final WarehouseMapper warehouseMapper;
    private final StockRepository stockRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final InventoryMovementTypeRepository inventoryMovementTypeRepository;
    private final InventoryMovementRepository inventoryMovementRepository;

    public void handleNewWareHouse(WareHouseMP wareHouse) {
        LOGGER.info("Processing new warehouse: {}", wareHouse);
        validateWareHouseRequest(wareHouse);
        WareHouse savedWareHouse = createAndSaveWareHouse(wareHouse);
        LOGGER.info("Successfully processed new warehouse with ID: {}", savedWareHouse.getWareHouseId());
    }

    @Transactional
    public void handleDeleteWareHouse(String id) {
        LOGGER.info("Processing warehouse deletion for ID: {}", id);
        try {
            int wareHouseId = parseWareHouseId(id);
            if (!wareHouseRepository.existsById(wareHouseId)) {
                throw WareHouseException.notFound(wareHouseId);
            }
            employeeRepository.updateEmployeeWarehouseToNull(wareHouseId);
            LOGGER.info("Successfully updated all employees warehouse to null for wareHouse with ID: {}", id);

            stockRepository.deleteAllByWareHouseId(wareHouseId);
            LOGGER.info("Successfully deleted all stocks for wareHouse with ID: {}", id);

            wareHouseRepository.deleteById(wareHouseId);
            LOGGER.info("Successfully deleted wareHouse with ID: {}", id);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid wareHouse ID format: " + id);
        }
    }

    public WareHouse getWareHouseById(String id) {
        LOGGER.info("Fetching warehouse with ID: {}", id);
        int wareHouseId = parseWareHouseId(id);
        return getWareHouseById(wareHouseId);
    }

    public List<WareHouse> getAllWareHouse() {
        LOGGER.info("Fetching all warehouses");
        List<WareHouse> wareHouses = wareHouseRepository.findAll();
        LOGGER.info("Successfully retrieved {} warehouses", wareHouses.size());
        return wareHouses;
    }


    public void handleWareHouseTransaction(WhTransferMessage whTransferMessage) {
        LOGGER.info("Processing warehouse transaction: {}", whTransferMessage);

        // We're going to check if from_ware_house_id exists
        WareHouse fromWareHouse = getWareHouseById(whTransferMessage.getFrom_ware_house_id());
        // We're going to check if to_ware_house_id exists
        WareHouse toWareHouse = getWareHouseById(whTransferMessage.getTo_ware_house_id());
        // We're going to check if product_id exists
        Product product = productRepository.findByProductId(whTransferMessage.getProduct_id());
        if (product == null) {
            throw ProductException.notFound(whTransferMessage.getProduct_id());
        }
        // then we're going to validate that quantity is not <= 0
        if (whTransferMessage.getQuantity() <= 0) {
            LOGGER.error("Quantity must be greater than 0");
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        // If everything good we create InventoryMovement with TRANSFER OUT
        LOGGER.info("Creating TRANSFER OUT inventory movement for product with id: {}", whTransferMessage.getProduct_id());
        InventoryMovement transfer_out_movement = InventoryMovement.builder()
                .wareHouse(fromWareHouse)
                .product(product)
                .quantity(whTransferMessage.getQuantity()).from_warehouse(fromWareHouse)
                .to_warehouse(toWareHouse).inventoryMovementType(inventoryMovementTypeRepository.findByInventoryMovementTypeId(
                        InventoryMovementReferenceType.TRANSFER_OUT.getType_id()))
                .reference_type(InventoryMovementReferenceType.TRANSFER_OUT.getValue())
                .notes(String.format("Creating TRANSFER OUT inventory movement for product with id: %s", whTransferMessage.getProduct_id())).build();

        LOGGER.info("Successfully created TRANSFER OUT inventory movement for product with id: {}", whTransferMessage.getProduct_id());

        inventoryMovementRepository.save(transfer_out_movement);
        // we subtract from_ware_house_stock and add to_ware_house_stock quantity of that product_id
        Stock stock_out = stockRepository.findByProductIdAndWareHouseId(whTransferMessage.getProduct_id(), whTransferMessage.getFrom_ware_house_id());
        stock_out.setQuantity(stock_out.getQuantity() - whTransferMessage.getQuantity());
        stockRepository.save(stock_out);
        fromWareHouse.setWareHouseCapacity(fromWareHouse.getWareHouseCapacity() - whTransferMessage.getQuantity());
        wareHouseRepository.save(fromWareHouse);
        LOGGER.info("Successfully created TRANSFER OUT inventory movement for product with id: {}", whTransferMessage.getProduct_id());
        if (stock_out.getQuantity() <= 0) {
            stockRepository.delete(stock_out);
            LOGGER.info("Successfully deleted stock for product with id: {}", whTransferMessage.getProduct_id());
        }

        Stock stock_in = stockRepository.findByProductIdAndWareHouseId(whTransferMessage.getProduct_id(), whTransferMessage.getTo_ware_house_id());
        if (stock_in == null) {
            // That means it does not exist
            Stock stock = Stock.builder().product(product).quantity(whTransferMessage.getQuantity()).wareHouse(toWareHouse).build();
            stockRepository.save(stock);
        }
        toWareHouse.setWareHouseCapacity(toWareHouse.getWareHouseCapacity() + whTransferMessage.getQuantity());
        wareHouseRepository.save(toWareHouse);

        // Lastly, we do TRANSFER IN of inventory movement
        LOGGER.info("Creating TRANSFER IN inventory movement for product with id: {}", whTransferMessage.getProduct_id());
        InventoryMovement transfer_in_movement = InventoryMovement.builder()
                .wareHouse(toWareHouse)
                .product(product)
                .quantity(whTransferMessage.getQuantity()).inventoryMovementType(inventoryMovementTypeRepository.findByInventoryMovementTypeId(
                        InventoryMovementReferenceType.TRANSFER_IN.getType_id()))
                .reference_type(InventoryMovementReferenceType.TRANSFER_IN.getValue())
                .notes(String.format("Creating TRANSFER IN inventory movement for product with id: %s", whTransferMessage.getProduct_id())).build();

        inventoryMovementRepository.save(transfer_in_movement);
        LOGGER.info("Successfully created TRANSFER IN inventory movement for product with id: {}", whTransferMessage.getProduct_id());
    }

    // Helper Methods ---------------------------------------------------------------------------------------

    private WareHouse getWareHouseById(int wareHouseId) {
        LOGGER.debug("Fetching warehouse with ID: {}", wareHouseId);

        Optional<WareHouse> wareHouse = wareHouseRepository.findById(wareHouseId);
        if (wareHouse.isEmpty()) {
            LOGGER.error("WareHouse not found with ID: {}", wareHouseId);
            throw WareHouseException.notFound(wareHouseId);
        }

        LOGGER.debug("WareHouse found successfully with ID: {}", wareHouseId);
        return wareHouse.get();
    }

    private void validateWareHouseRequest(WareHouseMP wareHouseRequest) {
        try {
            wareHouseValidator.validate(wareHouseRequest);
            LOGGER.debug("Warehouse validation successful for: {}", wareHouseRequest.getWare_house_name());
        } catch (Exception e) {
            LOGGER.error("Warehouse validation failed: {}", e.getMessage());
            throw e;
        }
    }

    private WareHouse createAndSaveWareHouse(WareHouseMP wareHouseRequest) {
        try {
            WareHouse newWareHouse = warehouseMapper.toEntity(wareHouseRequest);
            wareHouseRepository.save(newWareHouse);
            LOGGER.info("Successfully saved new warehouse with name: {}", wareHouseRequest.getWare_house_name());
            return newWareHouse;
        } catch (Exception e) {
            LOGGER.error("Failed to save warehouse: {}", e.getMessage());
            throw new RuntimeException("Failed to save warehouse", e);
        }
    }

    private int parseWareHouseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid warehouse ID format: {}", id);
            throw new IllegalArgumentException("Invalid warehouse ID format: " + id);
        }
    }
}
