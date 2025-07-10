package com.example.orderprocessingservice.config.database;

import com.example.orderprocessingservice.config.database.loaders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CustomerLoader customerLoader;
    private final EmployeeLoader employeeLoader;
    private final ProductLoader productLoader;
    private final StockLoader stockLoader;
    private final SupplierLoader supplierLoader;
    private final WareHouseLoader wareHouseLoader;
    private final DatabaseCleanupService databaseCleanupService;


    @Override
    public void run(String... args) throws Exception {
        databaseCleanupService.truncateWareHouseTables();
        databaseCleanupService.truncateProductsTables();
        databaseCleanupService.truncateCustomersTables();
        databaseCleanupService.truncateSupplierTables();

        wareHouseLoader.loadData();
        customerLoader.loadData();
        supplierLoader.loadData();
        productLoader.loadData();
        employeeLoader.loadData();
        stockLoader.loadData();
    }

}
