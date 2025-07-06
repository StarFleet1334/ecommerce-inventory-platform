package org.example

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently
import org.apache.rocketmq.common.message.MessageExt
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service
class GraphsMessageConsumer(
    private val consumer: DefaultMQPushConsumer
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        try {
            logger.info("Initializing RocketMQ consumer with group: ${consumer.consumerGroup}")
            val topics = listOf(
                "customer_add",
                "customer_delete",
                "employee_add",
                "employee_delete",
                "product_add",
                "product_delete",
                "stock_add",
                "stock_delete",
                "supplier_add",
                "supplier_delete",
                "warehouse_add",
                "warehouse_delete",
                "customer_order",
                "supply_add",
                "supply_delete"
            )

            topics.forEach { topic ->
                consumer.subscribe(topic, "*")
            }

            consumer.registerMessageListener(MessageListenerConcurrently { msgs, context ->
                msgs.forEach { msg ->
                    try {
                        processMessage(msg)
                    } catch (e: Exception) {
                        logger.error("Error processing message: ${e.message}", e)
                    }
                }
                ConsumeConcurrentlyStatus.CONSUME_SUCCESS
            })


            consumer.start()
            logger.info("RocketMQ consumer started successfully")
        } catch (e: Exception) {
            logger.error("Error starting RocketMQ consumer: ${e.message}", e)
            logger.error("Consumer configuration: group=${consumer.consumerGroup}, nameServer=${consumer.namesrvAddr}")
            throw RuntimeException("Failed to start RocketMQ consumer", e)
        }

    }

    private fun processMessage(message: MessageExt) {
        val topic = message.topic
        val content = String(message.body)

        logger.info("Received message from topic: $topic")
        logger.debug("Message content: $content")

        when (topic) {
            "customer_add" -> handleCustomerAdd(content)
            "customer_delete" -> handleCustomerDelete(content)
            "employee_add" -> handleEmployeeAdd(content)
            "employee_delete" -> handleEmployeeDelete(content)
            "product_add" -> handleProductAdd(content)
            "product_delete" -> handleProductDelete(content)
            "stock_add" -> handleStockAdd(content)
            "stock_delete" -> handleStockDelete(content)
            "supplier_add" -> handleSupplierAdd(content)
            "supplier_delete" -> handleSupplierDelete(content)
            "warehouse_add" -> handleWarehouseAdd(content)
            "warehouse_delete" -> handleWarehouseDelete(content)
            "customer_order" -> handleCustomerOrder(content)
            "supply_add" -> handleSupplyAdd(content)
            "supply_delete" -> handleSupplyDelete(content)
        }
    }

    private fun handleCustomerAdd(content: String) {
    }

    private fun handleCustomerDelete(content: String) {
    }

    private fun handleEmployeeAdd(content: String) {

    }

    private fun handleEmployeeDelete(content: String) {

    }

    private fun handleProductAdd(content: String) {

    }

    private fun handleProductDelete(content: String) {

    }

    private fun handleStockAdd(content: String) {

    }

    private fun handleStockDelete(content: String) {

    }

    private fun handleSupplierAdd(content: String) {

    }

    private fun handleSupplierDelete(content: String) {

    }

    private fun handleWarehouseAdd(content: String) {

    }

    private fun handleWarehouseDelete(content: String) {

    }

    private fun handleCustomerOrder(content: String) {

    }

    private fun handleSupplyAdd(content: String) {

    }

    private fun handleSupplyDelete(content: String) {

    }


    @PreDestroy
    fun destroy() {
        consumer.shutdown()
        logger.info("RocketMQ consumer shutdown completed")
    }
}