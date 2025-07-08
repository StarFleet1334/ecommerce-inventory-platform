package org.example.handler.addHandlers.supplier

import org.example.handler.base.AddOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SupplierAddHandler : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("SupplierAddHandler initialized")
    }

    override fun processAdd(message: String) {
        logger.info("Processing supplier add message: $message")
        // Implementation
    }
}