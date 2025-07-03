package com.example.inventoryservice.event.publisher;

import com.example.inventoryservice.entity.messages.SupplyMessage;
import com.example.inventoryservice.event.base.AbstractEventPBSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Service;

@Service
public class SupplyEventPB extends AbstractEventPBSender<SupplyMessage> {
    public SupplyEventPB(DefaultMQProducer producer, ObjectMapper objectMapper) {
        super(producer, objectMapper);
    }
}
