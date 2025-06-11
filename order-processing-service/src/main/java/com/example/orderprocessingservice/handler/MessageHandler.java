package com.example.orderprocessingservice.handler;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

@Component
public interface MessageHandler {
    ConsumeConcurrentlyStatus handleMessage(MessageExt message);
}
