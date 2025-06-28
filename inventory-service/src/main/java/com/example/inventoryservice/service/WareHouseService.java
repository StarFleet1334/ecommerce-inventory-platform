package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.messages.WareHouseMessage;
import com.example.inventoryservice.event.publisher.WareHouseEventPB;
import com.example.inventoryservice.event.remover.WareHouseEventRM;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WareHouseService {

    private final WareHouseEventPB wareHouseEventPB;
    private final WareHouseEventRM wareHouseEventRM;

    @Value("${rocketmq.warehouse_add_topic}")
    private String ADD_WAREHOUSE_TOPIC;

    @Value("${rocketmq.warehouse_delete_topic}")
    private String DELETE_WAREHOUSE_TOPIC;

    public void sendWareHouseCreateMessage(WareHouseMessage wareHouse) {
        wareHouseEventPB.sentMessage(ADD_WAREHOUSE_TOPIC, EventType.CREATED.getMessage(), wareHouse);
    }

    public void sendWareHouseDeleteMessage(String wareHouseId) {
        wareHouseEventRM.sentMessage(DELETE_WAREHOUSE_TOPIC, EventType.DELETED.getMessage(), wareHouseId);
    }

}
