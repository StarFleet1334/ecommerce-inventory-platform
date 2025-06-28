package com.example.inventoryservice.event.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public abstract class AbstractEventPBSender<T> implements BaseEventPB<T> {

    private final DefaultMQProducer producer;
    private final ObjectMapper objectMapper;

    @Override
    public void sentMessage(String topic, String tag, T data) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(data);
            Message message = new Message(topic, tag, jsonResponse.getBytes(StandardCharsets.UTF_8));
            producer.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message to RocketMQ", e);
        }
    }
}

