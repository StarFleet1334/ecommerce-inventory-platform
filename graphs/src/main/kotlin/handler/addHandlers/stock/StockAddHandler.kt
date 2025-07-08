package org.example.handler.addHandlers.stock

import org.example.handler.base.AddOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StockAddHandler : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("StockAddHandler initialized")
    }

    override fun processAdd(message: String) {
        logger.info("Processing stock add message: $message")
        // Implementation
    }
}