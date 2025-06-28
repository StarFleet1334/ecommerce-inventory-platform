package com.example.orderprocessingservice.handler.asset;

import com.example.orderprocessingservice.dto.mapped.ProductMP;
import com.example.orderprocessingservice.handler.MessageHandler;
import com.example.orderprocessingservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddProductMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddProductMessageHandler.class);
    private final ProductService productService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String messageBody = new String(message.getBody());
            LOGGER.info("Processing ADD product message: {}", messageBody);

            ProductMP product_message = objectMapper.readValue(messageBody, ProductMP.class);
            productService.handleNewProduct(product_message);

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing ADD product message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
