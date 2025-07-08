package org.example.handler.addHandlers.customers

import org.example.handler.base.AddOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CustomerAddHandler : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("CustomerAddHandler initialized")
    }

    override fun processAdd(message: String) {
        logger.info("Processing customer add message: $message")
        // Implementation
    }

}