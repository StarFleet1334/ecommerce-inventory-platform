package com.example.inventoryservice.event.base;

import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

@RequiredArgsConstructor
public abstract class BaseEventRM {

    private final DefaultMQProducer producer;

    public void sentMessage(String topic, String tag, String id) {
        try {
            Message message = new Message(topic, tag, id.getBytes());
            producer.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message to RocketMQ", e);
        }
    }

}
