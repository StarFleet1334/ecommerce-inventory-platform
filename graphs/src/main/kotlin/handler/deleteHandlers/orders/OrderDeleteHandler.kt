package org.example.handler.deleteHandlers.orders

import org.example.handler.base.DeleteOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderDeleteHandler : DeleteOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("OrderDeleteHandler initialized")
    }

    override fun processDelete(message: String) {
        logger.info("Processing order delete message: $message")
        // Implementation
    }
}