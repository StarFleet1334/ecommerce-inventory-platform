package org.example.handler.addHandlers.product

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.handler.base.AddOperationHandler
import org.example.model.ProductModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class ProductAddHandler @Autowired constructor(
    private val objectMapper: ObjectMapper
) : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("ProductAddHandler initialized")
    }

    override fun processAdd(message: String) {
        try {
            logger.debug("Starting to process message: $message")
            val jsonNode = objectMapper.readTree(message)
            logger.debug("Parsed JSON: ${jsonNode.toPrettyString()}")

            val productModel = ProductModel(
                product_name = jsonNode.get("product_name").asText(),
                sku = jsonNode.get("sku").asText(),
                product_id = jsonNode.get("product_id").asText(),
                product_price = BigDecimal(jsonNode.get("product_price").asText()).setScale(6, RoundingMode.HALF_UP),
                product_description = jsonNode.get("product_description").asText()
            )

            logger.debug("Successfully created ProductModel: {}", productModel)
        } catch (e: Exception) {
            logger.error("Top-level error: ${e.javaClass.simpleName}: ${e.message}")
            throw e
        }
    }
}
