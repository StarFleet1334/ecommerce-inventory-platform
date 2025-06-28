package com.example.inventoryservice.event.publisher;

import com.example.inventoryservice.entity.messages.StockMessage;
import com.example.inventoryservice.event.base.AbstractEventPBSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Service;

@Service
public class StockEventPB extends AbstractEventPBSender<StockMessage> {

    public StockEventPB(DefaultMQProducer producer, ObjectMapper objectMapper) {
        super(producer, objectMapper);
    }
}
