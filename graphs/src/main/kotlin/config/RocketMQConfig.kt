package org.example.config

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
class RocketMQConfig {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${rocketmq.name-server}")
    private lateinit var nameServer: String

    @Value("\${rocketmq.graph.consumer.group}")
    private lateinit var consumerGroup: String

    @Bean
    fun defaultMQPushConsumer(): DefaultMQPushConsumer {
        logger.info("Creating RocketMQ consumer with group: $consumerGroup and nameServer: $nameServer")
        val consumer = DefaultMQPushConsumer(consumerGroup)

        var retryCount = 0
        val maxRetries = 5
        val retryDelay = 15000L

        while (retryCount < maxRetries) {
            try {
                consumer.apply {
                    namesrvAddr = nameServer
                    isVipChannelEnabled = false
                    maxReconsumeTimes = 3
                    messageModel = MessageModel.CLUSTERING
                    consumeTimeout = 15000
                }
                logger.info("Successfully connected to RocketMQ")
                break
            } catch (e: Exception) {
                retryCount++
                logger.warn("Failed to connect to RocketMQ (attempt $retryCount/$maxRetries): ${e.message}")
                if (retryCount == maxRetries) {
                    logger.error("Failed to connect to RocketMQ after $maxRetries attempts")
                    throw e
                }
                Thread.sleep(retryDelay)
            }
        }

        logger.info("RocketMQ consumer configuration complete")
        return consumer
    }

}