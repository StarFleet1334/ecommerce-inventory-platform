package org.example

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently
import org.apache.rocketmq.common.message.MessageExt
import org.example.constants.TopicNames
import org.example.handler.base.MessageHandlerFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service
class GraphsMessageConsumer(
    private val consumer: DefaultMQPushConsumer,
    private val handlerFactory: MessageHandlerFactory
) {
    private val logger = LoggerFactory.getLogger(javaClass)


    @PostConstruct
    fun init() {
        try {
            logger.info("Initializing RocketMQ consumer with group: ${consumer.consumerGroup}")
            val topics = listOf(
                TopicNames.CUSTOMER_ADD,
                TopicNames.CUSTOMER_DELETE,
                TopicNames.EMPLOYEE_ADD,
                TopicNames.EMPLOYEE_DELETE,
                TopicNames.PRODUCT_ADD,
                TopicNames.PRODUCT_DELETE,
                TopicNames.STOCK_ADD,
                TopicNames.STOCK_DELETE,
                TopicNames.SUPPLIER_ADD,
                TopicNames.SUPPLIER_DELETE,
                TopicNames.WAREHOUSE_ADD,
                TopicNames.WAREHOUSE_DELETE,
                TopicNames.CUSTOMER_ORDER,
                TopicNames.SUPPLY_ADD,
                TopicNames.SUPPLY_DELETE
            )

            topics.forEach { topic ->
                consumer.subscribe(topic, "*")
            }

            consumer.registerMessageListener(MessageListenerConcurrently { msgs, _ ->
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

        try {
            val handler = handlerFactory.getHandler(topic)
            handler.handle(content)
        } catch (e: Exception) {
            logger.error("Error processing message for topic $topic: ${e.message}", e)
        }

    }

    @PreDestroy
    fun destroy() {
        consumer.shutdown()
        logger.info("RocketMQ consumer shutdown completed")
    }
}