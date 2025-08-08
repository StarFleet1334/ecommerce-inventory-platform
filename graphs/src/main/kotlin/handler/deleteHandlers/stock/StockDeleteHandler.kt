package org.example.handler.deleteHandlers.stock

import org.example.handler.base.DeleteOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StockDeleteHandler : DeleteOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("StockDeleteHandler initialized")
    }

    override fun processDelete(message: String) {
        logger.info("Processing stock delete message: $message")
        // Implementation
    }
}
