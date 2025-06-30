package com.example.orderprocessingservice.config.database;

import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private StockRepository stockRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private SupplierRepository supplierRepository;
    @Autowired private WareHouseRepository wareHouseRepository;

    @Autowired private ObjectMapper objectMapper;

    @Autowired
    private DatabaseCleanupService databaseCleanupService;


    @Override
    public void run(String... args) throws Exception {
        databaseCleanupService.truncateWareHouseTables();
        databaseCleanupService.truncateProductsTables();
        databaseCleanupService.truncateCustomersTables();
        databaseCleanupService.truncateSupplierTables();
        loadWareHouses();
        wareHouseRepository.flush();
        loadCustomers();
        loadSuppliers();
        loadProducts();
        loadEmployeesWithWareHouse();
        loadStockWithMappings();
    }

    private void loadWareHouses() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/warehouses.json");
        List<WareHouse> warehouses = objectMapper.readValue(is, new TypeReference<>() {});
        wareHouseRepository.saveAll(warehouses);
    }

    private void loadCustomers() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/customers.json");
        List<Customer> customers = objectMapper.readValue(is, new TypeReference<>() {});
        customerRepository.saveAll(customers);
    }

    private void loadSuppliers() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/suppliers.json");
        List<Supplier> suppliers = objectMapper.readValue(is, new TypeReference<>() {});
        supplierRepository.saveAll(suppliers);
    }

    private void loadProducts() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/products.json");
        List<Product> products = objectMapper.readValue(is, new TypeReference<>() {});
        productRepository.saveAll(products);
    }

    private void loadEmployeesWithWareHouse() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/employees.json");
        List<Employee> employees = objectMapper.readValue(is, new TypeReference<>() {});

        for (Employee emp : employees) {
            Integer warehouseId = emp.getWareHouseId();
            if (warehouseId == null) {
                throw new IllegalStateException("Missing wareHouseId for employee: " + emp.getEmail());
            }

            WareHouse wareHouse = wareHouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new RuntimeException("WareHouse not found with id = " + warehouseId));
            emp.setWareHouse(wareHouse);
        }

        employeeRepository.saveAll(employees);
    }

    private void loadStockWithMappings() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data/stock.json");
        List<Map<String, Object>> stockEntries = objectMapper.readValue(is, new TypeReference<>() {});

        List<Stock> validStocks = new ArrayList<>();
        Map<Integer, Integer> warehouseTotals = new HashMap<>();

        for (Map<String, Object> entry : stockEntries) {
            Integer wareHouseId = (Integer) entry.get("ware_house_id");
            String productId = (String) entry.get("product_id");
            Integer quantity = (Integer) entry.get("quantity");

            if (wareHouseId == null || productId == null || quantity == null) {
                System.out.println("⚠️ Skipping entry due to missing fields: " + entry);
                continue;
            }

            WareHouse wareHouse = wareHouseRepository.findById(wareHouseId)
                    .orElseThrow(() -> new RuntimeException("WareHouse not found with id: " + wareHouseId));

            Product product = productRepository.findByProductId(productId);
            if (product == null) {
                System.out.println("Product is not found: " + productId + "");
                continue;
            }

            int currentTotal = warehouseTotals.getOrDefault(wareHouseId, 0);
            int newTotal = currentTotal + quantity;

            if (newTotal > wareHouse.getMaxStockLevel()) {
                System.out.println("❌ Skipped: adding " + quantity + " to " + wareHouse.getWareHouseName() +
                        " exceeds max_stock_level (" + newTotal + "/" + wareHouse.getMaxStockLevel() + ")");
                continue;
            }

            Stock stock = Stock.builder()
                    .product(product)
                    .wareHouse(wareHouse)
                    .quantity(quantity)
                    .build();
            validStocks.add(stock);

            wareHouse.setWareHouseCapacity(newTotal);
            warehouseTotals.put(wareHouseId, newTotal);
        }

        if (!validStocks.isEmpty()) {
            stockRepository.saveAll(validStocks);
            Set<Integer> updatedWarehouses = warehouseTotals.keySet();
            List<WareHouse> warehousesToUpdate = wareHouseRepository.findAllById(updatedWarehouses);
            for (WareHouse warehouse : warehousesToUpdate) {
                warehouse.setWareHouseCapacity(warehouseTotals.get(warehouse.getWareHouseId()));
            }
            wareHouseRepository.saveAll(warehousesToUpdate);
        }
    }
}
