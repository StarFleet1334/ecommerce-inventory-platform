package com.example.inventoryservice.event;

import com.example.inventoryservice.entity.common.InventoryItem;
import com.example.inventoryservice.entity.response.InventoryItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryEventPublisher {
    private final DefaultMQProducer producer;

    public InventoryItemResponse sentMessage(String topic,String tag, InventoryItem payload) {
        InventoryItemResponse response;
        try {
            response = new InventoryItemResponse();
            response.setId(UUID.randomUUID().toString());
            response.setLastUpdated(LocalDateTime.now());

            response.setProductName(payload.getProductName());
            response.setSku(payload.getSku());
            response.setQuantity(payload.getQuantity());
            response.setWarehouseLocation(payload.getWarehouseLocation());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonResponse = objectMapper.writeValueAsString(response);

            Message message = new Message(topic, tag, jsonResponse.getBytes(StandardCharsets.UTF_8));
            producer.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send message to RocketMQ",e);
        }
        return response;
    }
}
