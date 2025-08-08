package org.example.config


import org.example.handler.addHandlers.customers.CustomerAddHandler
import org.example.handler.addHandlers.employees.EmployeeAddHandler
import org.example.handler.addHandlers.orders.OrderAddHandler
import org.example.handler.addHandlers.product.ProductAddHandler
import org.example.handler.addHandlers.stock.StockAddHandler
import org.example.handler.addHandlers.supplier.SupplierAddHandler
import org.example.handler.addHandlers.supply.SupplyAddHandler
import org.example.handler.addHandlers.warehouse.WareHouseAddHandler
import org.example.handler.base.MessageHandler
import org.example.handler.deleteHandlers.customers.CustomerDeleteHandler
import org.example.handler.deleteHandlers.employees.EmployeeDeleteHandler
import org.example.handler.deleteHandlers.product.ProductDeleteHandler
import org.example.handler.deleteHandlers.stock.StockDeleteHandler
import org.example.handler.deleteHandlers.supplier.SupplierDeleteHandler
import org.example.handler.deleteHandlers.supply.SupplyDeleteHandler
import org.example.handler.deleteHandlers.warehouse.WareHouseDeleteHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["org.example.handler"])
class MessageHandlerConfig {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun messageHandlers(
        customerAddHandler: CustomerAddHandler,
        customerDeleteHandler: CustomerDeleteHandler,
        employeeAddHandler: EmployeeAddHandler,
        employeeDeleteHandler: EmployeeDeleteHandler,
        productAddHandler: ProductAddHandler,
        productDeleteHandler: ProductDeleteHandler,
        stockAddHandler: StockAddHandler,
        stockDeleteHandler: StockDeleteHandler,
        supplierAddHandler: SupplierAddHandler,
        supplierDeleteHandler: SupplierDeleteHandler,
        warehouseAddHandler: WareHouseAddHandler,
        warehouseDeleteHandler: WareHouseDeleteHandler,
        customerOrderHandler: OrderAddHandler,
        supplyAddHandler: SupplyAddHandler,
        supplyDeleteHandler: SupplyDeleteHandler


    ): Map<String, MessageHandler> {
        return mapOf(
            "customer_add" to customerAddHandler,
            "customer_delete" to customerDeleteHandler,
            "employee_add" to employeeAddHandler,
            "employee_delete" to employeeDeleteHandler,
            "product_add" to productAddHandler,
            "product_delete" to productDeleteHandler,
            "stock_add" to stockAddHandler,
            "stock_delete" to stockDeleteHandler,
            "supplier_add" to supplierAddHandler,
            "supplier_delete" to supplierDeleteHandler,
            "warehouse_add" to warehouseAddHandler,
            "warehouse_delete" to warehouseDeleteHandler,
            "customer_order" to customerOrderHandler,
            "supply_add" to supplyAddHandler,
            "supply_delete" to supplyDeleteHandler
        ).also {
            logger.info("Created handlers map with keys: ${it.keys}")
        }

    }

}
