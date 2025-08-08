package org.example.handler.addHandlers.employees

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.handler.base.AddOperationHandler
import org.example.model.EmployeeModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EmployeeAddHandler @Autowired constructor(
    private val objectMapper: ObjectMapper
) : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(EmployeeAddHandler::class.java)

    init {
        logger.info("EmployeeAddHandler initialized")
    }

    override fun processAdd(message: String) {
        try {
            logger.debug("Starting to process message: $message")
            val jsonNode = objectMapper.readTree(message)
            logger.debug("Parsed JSON: ${jsonNode.toPrettyString()}")

            val employeeModel = EmployeeModel(
                first_name = jsonNode.get("first_name").asText(),
                last_name = jsonNode.get("last_name").asText(),
                email = jsonNode.get("email").asText(),
                phone_number = jsonNode.get("phone_number").asText(),
                ware_house_id = jsonNode.get("ware_house_id").asInt()
            )

            logger.debug("Successfully created EmployeeModel: {}", employeeModel)
        } catch (e: Exception) {
            logger.error("Top-level error: ${e.javaClass.simpleName}: ${e.message}")
            throw e
        }
    }
}
