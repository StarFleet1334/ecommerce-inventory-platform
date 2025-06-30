package com.example.orderprocessingservice.handler.customer;

import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
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
public class AddCustomerOrderMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCustomerOrderMessageHandler.class);
    private final CustomerService customerService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String messageBody = new String(message.getBody());
            LOGGER.info("Processing customer order message: {}", messageBody);

            CustomerOrderMP customer_order_message = objectMapper.readValue(messageBody, CustomerOrderMP.class);
            customerService.handleCustomerNewOrder(customer_order_message);

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing customer order message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
