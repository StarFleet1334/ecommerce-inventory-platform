package org.example.handler.deleteHandlers.employees

import org.example.handler.base.DeleteOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EmployeeDeleteHandler : DeleteOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("EmployeeDeleteHandler initialized")
    }

    override fun processDelete(message: String) {
        logger.info("Processing employee delete message: $message")
        // Implementation
    }
}
