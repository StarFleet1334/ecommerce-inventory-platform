package com.example.orderprocessingservice.handler;

import com.example.orderprocessingservice.handler.asset.*;
import com.example.orderprocessingservice.handler.customer.AddCustomerMessageHandler;
import com.example.orderprocessingservice.handler.customer.AddCustomerOrderMessageHandler;
import com.example.orderprocessingservice.handler.customer.DeleteCustomerMessageHandler;
import com.example.orderprocessingservice.handler.personnel.AddEmployeeMessageHandler;
import com.example.orderprocessingservice.handler.personnel.AddWareHouseMessageHandler;
import com.example.orderprocessingservice.handler.personnel.DeleteEmployeeMessageHandler;
import com.example.orderprocessingservice.handler.personnel.DeleteWareHouseMessageHandler;
import com.example.orderprocessingservice.handler.supplier.AddSupplierMessageHandler;
import com.example.orderprocessingservice.handler.supplier.DeleteSupplierMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageHandlerFactory {
    private Map<String, MessageHandler> handlers;

    // Product Handlers
    private final AddProductMessageHandler addProductMessageHandler;
    private final DeleteProductMessageHandler deleteProductMessageHandler;

    // Stock Handlers
    private final AddStockMessagehandler addStockMessagehandler;
    private final DeleteStockMessageHandler deleteStockMessageHandler;

    // Customer Handlers
    private final AddCustomerMessageHandler addCustomerMessageHandler;
    private final DeleteCustomerMessageHandler deleteCustomerMessageHandler;

    // Employee Handlers
    private final AddEmployeeMessageHandler addEmployeeMessageHandler;
    private final DeleteEmployeeMessageHandler deleteEmployeeMessageHandler;

    // WareHouse Handlers
    private final AddWareHouseMessageHandler addWareHouseMessageHandler;
    private final DeleteWareHouseMessageHandler deleteWareHouseMessageHandler;

    // Supplier Handlers
    private final AddSupplierMessageHandler addSupplierMessageHandler;
    private final DeleteSupplierMessageHandler deleteSupplierMessageHandler;

    // Customer Order Handler
    private final AddCustomerOrderMessageHandler addCustomerOrderMessageHandler;

    // Supply Handlers
    private final AddSupplyMessageHandler addSupplyMessageHandler;
    private final DeleteSupplyMessageHandler deleteSupplyMessageHandler;


    @PostConstruct
    public void init() {
        handlers = new HashMap<>();

        // Product Handlers
        handlers.put("product_add",addProductMessageHandler);
        handlers.put("product_delete",deleteProductMessageHandler);

        // Stock Handlers
        handlers.put("stock_add",addStockMessagehandler);
        handlers.put("stock_delete",deleteStockMessageHandler);

        // Customer Handlers
        handlers.put("customer_add",addCustomerMessageHandler);
        handlers.put("customer_delete",deleteCustomerMessageHandler);

        // Employee Handlers
        handlers.put("employee_add",addEmployeeMessageHandler);
        handlers.put("employee_delete",deleteEmployeeMessageHandler);

        // WareHouse Handlers
        handlers.put("warehouse_add",addWareHouseMessageHandler);
        handlers.put("warehouse_delete",deleteWareHouseMessageHandler);

        // Supplier Handlers
        handlers.put("supplier_add",addSupplierMessageHandler);
        handlers.put("supplier_delete",deleteSupplierMessageHandler);

        // Customer Order Handlers
        handlers.put("customer_order",addCustomerOrderMessageHandler);

        // Supply Handlers
        handlers.put("supply_add",addSupplyMessageHandler);
        handlers.put("supply_delete",deleteSupplyMessageHandler);
    }

    public MessageHandler getHandler(String topic) {
        return handlers.get(topic);
    }
}
