package org.example.handler.deleteHandlers.customers

import org.example.handler.base.DeleteOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CustomerDeleteHandler : DeleteOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("CustomerDeleteHandler initialized")
    }

    override fun processDelete(message: String) {
        logger.info("Processing customer delete message: $message")
        // Implementation
    }
}