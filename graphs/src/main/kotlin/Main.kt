package org.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.html.*
import kotlinx.html.*

fun main() {
    embeddedServer(Netty, port = 8090) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            call.respondHtml {
                head {
                    title("Analytics Dashboard")
                    script(src = "https://cdn.jsdelivr.net/npm/chart.js") {}
                    style {
                        +"""
                        .graph-container {
                            width: 600px;
                            margin: 20px;
                            padding: 15px;
                            border: 1px solid #ddd;
                            border-radius: 5px;
                        }
                        .dashboard-title {
                            text-align: center;
                            margin: 20px 0;
                        }
                        """
                    }
                }
            }
        }
    }
}