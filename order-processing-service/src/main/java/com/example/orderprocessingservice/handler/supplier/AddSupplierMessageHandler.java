package com.example.orderprocessingservice.handler.supplier;

import com.example.orderprocessingservice.dto.eventDto.SupplierMP;
import com.example.orderprocessingservice.handler.MessageHandler;
import com.example.orderprocessingservice.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddSupplierMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddSupplierMessageHandler.class);
    private final SupplierService supplierService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String messageBody = new String(message.getBody());
            LOGGER.info("Processing ADD supplier message: {}", messageBody);

            SupplierMP supplier_message = objectMapper.readValue(messageBody, SupplierMP.class);
            supplierService.handleNewSupplier(supplier_message);

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing ADD supplier message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
