package com.example.orderprocessingservice.handler;

import com.example.orderprocessingservice.dto.InventoryMessage;
import com.example.orderprocessingservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddInventoryMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddInventoryMessageHandler.class);
    private final InventoryService inventoryService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String messageBody = new String(message.getBody());
            LOGGER.info("Processing ADD inventory message: {}", messageBody);

            InventoryMessage inventoryMessage = objectMapper.readValue(messageBody, InventoryMessage.class);
            inventoryService.handleNewInventory(inventoryMessage);

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing ADD inventory message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}

