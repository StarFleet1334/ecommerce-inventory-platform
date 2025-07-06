package org.example.config

import org.example.routes.DashboardRoute
import org.example.routes.dashboards.customers.CustomersDashboardRoute
import org.example.routes.dashboards.employees.EmployeesDashboardRoute
import org.example.routes.dashboards.inventory.InventoryDashboardRoute
import org.example.routes.dashboards.orders.OrdersDashboardRoute
import org.example.routes.dashboards.revenue.RevenueDashboardRoute
import org.example.routes.dashboards.supply.SupplyDashboardRoute
import org.example.routes.dashboards.transactions.TransactionsDashboardRoute

object DashboardRegistry {
    val dashboards : List<DashboardRoute> = listOf(
        InventoryDashboardRoute(),
        SupplyDashboardRoute(),
        OrdersDashboardRoute(),
        CustomersDashboardRoute(),
        EmployeesDashboardRoute(),
        TransactionsDashboardRoute(),
        RevenueDashboardRoute()
    )
}