package com.example.inventoryservice.event.publisher;

import com.example.inventoryservice.entity.messages.ProductMessage;
import com.example.inventoryservice.event.base.AbstractEventPBSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Service;

@Service
public class ProductEventPB extends AbstractEventPBSender<ProductMessage> {

    public ProductEventPB(DefaultMQProducer producer, ObjectMapper objectMapper) {
        super(producer, objectMapper);
    }
}
