package com.example.orderprocessingservice.config.database;

import com.example.orderprocessingservice.config.database.impl.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);
    private static final long DELAY_BETWEEN_LOADS = 10;

    private final CustomerLoaderImpl customerLoader;
    private final EmployeeLoaderImpl employeeLoader;
    private final ProductLoaderImpl productLoader;
    private final StockLoaderImpl stockLoader;
    private final SupplierLoaderImpl supplierLoader;
    private final WareHouseLoaderImpl wareHouseLoader;
    private final DatabaseCleanupService databaseCleanupService;

    private void sleep() {
        try {
            Thread.sleep(DELAY_BETWEEN_LOADS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Sleep interrupted between data loads", e);
        }
    }


    @Override
    public void run(String... args) throws Exception {
        databaseCleanupService.truncateWareHouseTables();
        sleep();
        databaseCleanupService.truncateProductsTables();
        sleep();
        databaseCleanupService.truncateCustomersTables();
        sleep();
        databaseCleanupService.truncateSupplierTables();
        sleep();
        databaseCleanupService.truncateEmployeeTables();
        sleep();
        databaseCleanupService.truncateStockTables();
        sleep();

        LOGGER.info("Starting data loading sequence...");

        LOGGER.info("Loading warehouse data...");
        wareHouseLoader.loadData();
        sleep();

        LOGGER.info("Loading customer data...");
        customerLoader.loadData();
        sleep();

        LOGGER.info("Loading supplier data...");
        supplierLoader.loadData();
        sleep();

        LOGGER.info("Loading product data...");
        productLoader.loadData();
        sleep();

        LOGGER.info("Loading employee data...");
        employeeLoader.loadData();
        sleep();

        LOGGER.info("Loading stock data...");
        stockLoader.loadData();

        LOGGER.info("Data loading sequence completed successfully");

    }

}
