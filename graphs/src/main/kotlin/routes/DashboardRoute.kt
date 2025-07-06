package org.example.routes

import io.ktor.server.routing.Routing

interface DashboardRoute {
    fun Routing.configureRoute()
    val path: String
    val pageTitle: String
}