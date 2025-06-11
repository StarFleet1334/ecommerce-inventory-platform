package com.example.orderprocessingservice.consumer;

import com.example.orderprocessingservice.handler.MessageHandler;
import com.example.orderprocessingservice.handler.MessageHandlerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessageConsumer.class);
    private final DefaultMQPushConsumer consumer;
    private final ObjectMapper objectMapper;
    private final MessageHandlerFactory messageHandlerFactory;

    @Value("${rocketmq.inventory_add_topic}")
    private String inventoryAddTopic;

    @Value("${rocketmq.inventory_delete_topic}")
    private String inventoryDeleteTopic;

    @PostConstruct
    public void init() throws Exception {
        consumer.subscribe(inventoryAddTopic, "*");
        consumer.subscribe(inventoryDeleteTopic, "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                String topic = msg.getTopic();
                LOGGER.info("Received message from topic: {}", topic);
                MessageHandler handler = messageHandlerFactory.getHandler(topic);

                if (handler != null) {
                    return handler.handle(msg, objectMapper);
                } else {
                    LOGGER.error("No handler found for topic: {}", topic);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
        LOGGER.info("RocketMQ consumer started for topics: {}, {}",
                inventoryAddTopic, inventoryDeleteTopic);
    }

    @PreDestroy
    public void destroy() {
        Optional.ofNullable(consumer).ifPresent(DefaultMQPushConsumer::shutdown);
    }
}