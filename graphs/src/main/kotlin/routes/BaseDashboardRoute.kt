package org.example.routes

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*

abstract class BaseDashboardRoute : DashboardRoute {
    override fun Routing.configureRoute() {
        get(path) {
            call.respondHtml {
                head {
                    title { +pageTitle }
                    script(src = "https://cdn.jsdelivr.net/npm/chart.js") {}
                    commonStyles()
                }
                body {
                    h1(classes = "dashboard-title") { +pageTitle }
                    div(classes = "content-container") {

                    }
                }
            }
        }
    }

    private fun HEAD.commonStyles() {
        style {
            +"""
            .content-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }
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
                color: #333;
                font-size: 24px;
            }
            """
        }
    }
}