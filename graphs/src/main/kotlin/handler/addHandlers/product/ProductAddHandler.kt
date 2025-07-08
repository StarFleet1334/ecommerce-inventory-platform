package org.example.handler.addHandlers.product

import org.example.handler.base.AddOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ProductAddHandler : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("ProductAddHandler initialized")
    }

    override fun processAdd(message: String) {
        logger.info("Processing product add message: $message")
        // Implementation
    }
}