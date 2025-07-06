package org.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.routing
import org.example.config.DashboardRegistry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.AnnotationConfigApplicationContext

@SpringBootApplication
open class Application

fun main() {
    val springContext = AnnotationConfigApplicationContext().apply {
        scan("org.example")
        refresh()
    }

    embeddedServer(Netty, port = 8090) {
        routing {
            DashboardRegistry.dashboards.forEach { dashboard ->
                with(dashboard) {
                    configureRoute()
                }
            }
        }
    }.start(wait = true)
}