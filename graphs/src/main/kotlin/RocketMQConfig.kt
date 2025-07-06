package org.example

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
open class RocketMQConfig {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${rocketmq.name-server}")
    private lateinit var nameServer: String

    @Value("\${rocketmq.consumer.group}")
    private lateinit var consumerGroup: String

    @Bean
    open fun defaultMQPushConsumer(): DefaultMQPushConsumer {
        logger.info("Creating RocketMQ consumer with group: $consumerGroup and nameServer: $nameServer")
        return DefaultMQPushConsumer(consumerGroup).apply {
            namesrvAddr = nameServer
            isVipChannelEnabled = false
            maxReconsumeTimes = 3
            messageModel = org.apache.rocketmq.common.protocol.heartbeat.MessageModel.CLUSTERING
            consumeTimeout = 15000
            logger.info("RocketMQ consumer configuration complete")
        }
    }
}