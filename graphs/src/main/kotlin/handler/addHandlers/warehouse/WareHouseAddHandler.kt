package org.example.handler.addHandlers.warehouse

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.handler.base.AddOperationHandler
import org.example.model.WareHouseModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class WareHouseAddHandler @Autowired constructor(
    private val objectMapper: ObjectMapper
) : AddOperationHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("WareHouseAddHandler initialized")
    }

    override fun processAdd(message: String) {
        try {
            logger.debug("Starting to process message: $message")
            val jsonNode = objectMapper.readTree(message)
            logger.debug("Parsed JSON: ${jsonNode.toPrettyString()}")

            val wareHouseModel = WareHouseModel(
                ware_house_name = jsonNode.get("ware_house_name").asText(),
                refrigerated = jsonNode.get("refrigerated").asBoolean(),
                min_stock_level = jsonNode.get("min_stock_level").asInt(),
                max_stock_level = jsonNode.get("max_stock_level").asInt(),
                latitude = BigDecimal(jsonNode.get("latitude").asText()).setScale(6, RoundingMode.HALF_UP),
                longitude = BigDecimal(jsonNode.get("longitude").asText()).setScale(6, RoundingMode.HALF_UP)
            )

            logger.debug("Successfully created WareHouseModel: {}", wareHouseModel)
        } catch (e: Exception) {
            logger.error("Top-level error: ${e.javaClass.simpleName}: ${e.message}")
            throw e
        }
    }
}
