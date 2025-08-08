package org.example.handler.addHandlers.stock

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.handler.base.AddOperationHandler
import org.example.model.StockModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StockAddHandler @Autowired constructor(
    private val objectMapper: ObjectMapper
) : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("StockAddHandler initialized")
    }

    override fun processAdd(message: String) {
        try {
            logger.debug("Starting to process message: $message")
            val jsonNode = objectMapper.readTree(message)
            logger.debug("Parsed JSON: ${jsonNode.toPrettyString()}")

            val stockModel = StockModel(
                ware_house_id = jsonNode.get("ware_house_id").asInt(),
                product_id = jsonNode.get("product_id").asText(),
                quantity = jsonNode.get("quantity").asInt()
            )

            logger.debug("Successfully created StockModel: {}", stockModel)
        } catch (e: Exception) {
            logger.error("Top-level error: ${e.javaClass.simpleName}: ${e.message}")
            throw e
        }
    }
}
