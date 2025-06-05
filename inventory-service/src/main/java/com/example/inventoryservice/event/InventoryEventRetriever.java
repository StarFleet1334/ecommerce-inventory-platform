package com.example.inventoryservice.event;

import com.example.inventoryservice.entity.response.InventoryItemResponse;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryEventRetriever {

    private final DefaultMQProducer producer;

    public List<InventoryItemResponse> sendMessage(String topicInventory, String tag) {
        try {
            Message msg = new Message(topicInventory, tag, "Retrieve all Inventory".getBytes(StandardCharsets.UTF_8));
            producer.send(msg);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send message to RocketMQ", e);
        }
        return null;
    }
}
