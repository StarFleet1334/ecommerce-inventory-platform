package org.example.handler.deleteHandlers.product

import org.example.handler.base.DeleteOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ProductDeleteHandler : DeleteOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("ProductDeleteHandler initialized")
    }

    override fun processDelete(message: String) {
        logger.info("Processing product delete message: $message")
        // Implementation
    }
}