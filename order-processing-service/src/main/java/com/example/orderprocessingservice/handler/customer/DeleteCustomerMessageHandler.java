package com.example.orderprocessingservice.handler.customer;

import com.example.orderprocessingservice.handler.MessageHandler;
import com.example.orderprocessingservice.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCustomerMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCustomerMessageHandler.class);
    private final CustomerService customerService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String id = new String(message.getBody());
            LOGGER.info("Processing DELETE customer message for ID: {}", id);

            // ... < service handles >

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing DELETE customer message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
