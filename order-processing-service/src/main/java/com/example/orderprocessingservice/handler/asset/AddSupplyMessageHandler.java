package com.example.orderprocessingservice.handler.asset;

import com.example.orderprocessingservice.dto.eventDto.SupplyMP;
import com.example.orderprocessingservice.handler.MessageHandler;
import com.example.orderprocessingservice.service.SupplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddSupplyMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddSupplyMessageHandler.class);
    private final SupplyService supplyService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String messageBody = new String(message.getBody());
            LOGGER.info("Processing ADD supply message: {}", messageBody);

            SupplyMP supply_message = objectMapper.readValue(messageBody, SupplyMP.class);

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing ADD product message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
