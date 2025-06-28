package com.example.inventoryservice.event.publisher;

import com.example.inventoryservice.entity.messages.CustomerMessage;
import com.example.inventoryservice.event.base.AbstractEventPBSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Service;

@Service
public class CustomerEventPB extends AbstractEventPBSender<CustomerMessage> {

    public CustomerEventPB(DefaultMQProducer producer, ObjectMapper objectMapper) {
        super(producer, objectMapper);
    }
}
