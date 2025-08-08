package org.example.handler.deleteHandlers.supply

import org.example.handler.base.DeleteOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SupplyDeleteHandler : DeleteOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("SupplyDeleteHandler initialized")
    }

    override fun processDelete(message: String) {
        logger.info("Processing supply delete message: $message")
        // Implementation
    }
}
