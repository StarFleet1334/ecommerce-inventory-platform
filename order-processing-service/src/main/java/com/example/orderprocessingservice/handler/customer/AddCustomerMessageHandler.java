package com.example.orderprocessingservice.handler.customer;

import com.example.orderprocessingservice.dto.eventDto.CustomerMP;
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
public class AddCustomerMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCustomerMessageHandler.class);
    private final CustomerService customerService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String messageBody = new String(message.getBody());
            LOGGER.info("Processing ADD customer message: {}", messageBody);

            CustomerMP customer_message = objectMapper.readValue(messageBody, CustomerMP.class);
            customerService.handleNewCustomer(customer_message);

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing ADD customer message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
