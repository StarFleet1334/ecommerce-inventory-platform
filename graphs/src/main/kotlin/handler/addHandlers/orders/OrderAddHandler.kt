package org.example.handler.addHandlers.orders

import org.example.handler.base.AddOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderAddHandler : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("OrderAddHandler initialized")
    }

    override fun processAdd(message: String) {
        logger.info("Processing order add message: $message")
        // Implementation
    }
}
