package org.example.routes.dashboards.customers

import org.example.routes.BaseDashboardRoute

class CustomersDashboardRoute : BaseDashboardRoute() {
    override val path = "/dashboards/customers"
    override val pageTitle = "Customer Demographics & Location"
}