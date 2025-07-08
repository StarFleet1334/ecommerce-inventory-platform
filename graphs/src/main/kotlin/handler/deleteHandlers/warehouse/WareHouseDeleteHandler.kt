package org.example.handler.deleteHandlers.warehouse

import org.example.handler.base.DeleteOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class WareHouseDeleteHandler : DeleteOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("WareHouseDeleteHandler initialized")
    }

    override fun processDelete(message: String) {
        logger.info("Processing warehouse delete message: $message")
        // Implementation
    }
}