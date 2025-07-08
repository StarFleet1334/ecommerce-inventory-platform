package org.example.handler.addHandlers.warehouse

import org.example.handler.base.AddOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class WareHouseAddHandler : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("WareHouseAddHandler initialized")
    }

    override fun processAdd(message: String) {
        logger.info("Processing warehouse add message: $message")
        // Implementation
    }
}