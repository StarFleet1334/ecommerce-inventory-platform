package org.example.handler.addHandlers.supplier

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.handler.base.AddOperationHandler
import org.example.model.SupplierModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class SupplierAddHandler @Autowired constructor(
    private val objectMapper: ObjectMapper
) : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("SupplierAddHandler initialized")
    }

    override fun processAdd(message: String) {
        try {
            logger.debug("Starting to process message: $message")
            val jsonNode = objectMapper.readTree(message)
            logger.debug("Parsed JSON: ${jsonNode.toPrettyString()}")

            val supplierModel = SupplierModel(
                first_name = jsonNode.get("first_name").asText(),
                last_name = jsonNode.get("last_name").asText(),
                email = jsonNode.get("email").asText(),
                phone_number = jsonNode.get("phone_number").asText(),
                latitude = BigDecimal(jsonNode.get("latitude").asText()).setScale(6, RoundingMode.HALF_UP),
                longitude = BigDecimal(jsonNode.get("longitude").asText()).setScale(6, RoundingMode.HALF_UP)
            )

            logger.debug("Successfully created SupplierModel: {}", supplierModel)
        } catch (e: Exception) {
            logger.error("Top-level error: ${e.javaClass.simpleName}: ${e.message}")
            throw e
        }
    }
}