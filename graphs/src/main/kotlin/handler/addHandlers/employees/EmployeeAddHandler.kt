package org.example.handler.addHandlers.employees

import org.example.handler.base.AddOperationHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EmployeeAddHandler : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(EmployeeAddHandler::class.java)

    init {
        logger.info("EmployeeAddHandler initialized")
    }

    override fun processAdd(message: String) {
        logger.info("Processing employee add message: $message")
        // Implementation
    }
}