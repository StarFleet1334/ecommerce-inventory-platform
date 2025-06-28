package com.example.inventoryservice.event.publisher;

import com.example.inventoryservice.entity.messages.WareHouseMessage;
import com.example.inventoryservice.event.base.AbstractEventPBSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Service;

@Service
public class WareHouseEventPB extends AbstractEventPBSender<WareHouseMessage> {

    public WareHouseEventPB(DefaultMQProducer producer, ObjectMapper objectMapper) {
        super(producer, objectMapper);
    }
}
