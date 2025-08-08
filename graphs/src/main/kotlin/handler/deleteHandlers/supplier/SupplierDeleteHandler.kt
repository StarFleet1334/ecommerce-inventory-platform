package org.example.handler.deleteHandlers.supplier

import org.example.handler.base.DeleteOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SupplierDeleteHandler : DeleteOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("SupplierDeleteHandler initialized")
    }

    override fun processDelete(message: String) {
        logger.info("Processing supplier delete message: $message")
        // Implementation
    }
}
