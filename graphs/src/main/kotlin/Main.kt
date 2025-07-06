package org.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.example.config.DashboardRegistry

fun main() {
    embeddedServer(Netty, port = 8090) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    routing {
        DashboardRegistry.dashboards.forEach { dashboard ->
            with(dashboard) {
                configureRoute()
            }
        }
    }

}