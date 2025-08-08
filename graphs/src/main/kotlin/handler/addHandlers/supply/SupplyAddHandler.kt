package org.example.handler.addHandlers.supply

import org.example.handler.base.AddOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SupplyAddHandler : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("SupplyAddHandler initialized")
    }

    override fun processAdd(message: String) {
        logger.info("Processing supply add message: $message")
        // Implementation
    }
}
