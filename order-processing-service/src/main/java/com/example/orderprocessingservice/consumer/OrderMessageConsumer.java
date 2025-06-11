package com.example.orderprocessingservice.consumer;

import com.example.orderprocessingservice.dto.InventoryMessage;
import com.example.orderprocessingservice.service.InventoryService;
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
@Component
@RequiredArgsConstructor
public class OrderMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessageConsumer.class);
    private final DefaultMQPushConsumer consumer;
    private final ObjectMapper objectMapper;
    private final InventoryService inventoryService;

    @Value("${rocketmq.topic}")
    private String topic;

    @PostConstruct
    public void init() throws Exception {
        consumer.subscribe(topic, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    String messageBody = new String(msg.getBody());
                    LOGGER.info("Received inventory message: {}", messageBody);

                    InventoryMessage inventoryMessage = objectMapper.readValue(messageBody, InventoryMessage.class);
                    processNewInventoryOrder(inventoryMessage);

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    LOGGER.error("Error processing inventory message: {}", e.getMessage(), e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
        LOGGER.info("RocketMQ consumer started for topic: {}", topic);
    }

    private void processNewInventoryOrder(InventoryMessage message) {
        LOGGER.info("Processing inventory update for product: {}, SKU: {}, Quantity: {}, Location: {}",
                message.getProductName(),
                message.getSku(),
                message.getQuantity(),
                message.getWarehouseLocation());

        inventoryService.handleNewInventory(message);
    }

    @PreDestroy
    public void destroy() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
}