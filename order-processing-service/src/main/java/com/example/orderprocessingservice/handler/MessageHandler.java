package com.example.orderprocessingservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

public interface MessageHandler {
    ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper);
}
