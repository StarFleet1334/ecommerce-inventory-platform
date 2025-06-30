package com.example.orderprocessingservice.handler.personnel;

import com.example.orderprocessingservice.dto.eventDto.WareHouseMP;
import com.example.orderprocessingservice.handler.MessageHandler;
import com.example.orderprocessingservice.service.WareHouseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddWareHouseMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddWareHouseMessageHandler.class);
    private final WareHouseService wareHouseService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String messageBody = new String(message.getBody());
            LOGGER.info("Processing ADD warehouse message: {}", messageBody);

            WareHouseMP ware_house_message = objectMapper.readValue(messageBody, WareHouseMP.class);
            wareHouseService.handleNewWareHouse(ware_house_message);

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing ADD warehouse message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
