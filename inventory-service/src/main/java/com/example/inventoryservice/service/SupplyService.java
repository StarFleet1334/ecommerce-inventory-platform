package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.messages.SupplyMessage;
import com.example.inventoryservice.event.publisher.SupplyEventPB;
import com.example.inventoryservice.event.remover.SupplyEventRM;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class SupplyService {

    private final SupplyEventPB supplyEventPB;
    private final SupplyEventRM customerEventRM;

    @Value("${rocketmq.supply_add_topic}")
    private String ADD_SUPPLY_TOPIC;

    @Value("${rocketmq.supply_delete_topic}")
    private String DELETE_SUPPLY_TOPIC;

    public void sendSupplyCreateMessage(SupplyMessage supplyMessage) {
        supplyMessage.setSupply_time(OffsetDateTime.now());
        supplyEventPB.sentMessage(ADD_SUPPLY_TOPIC, EventType.CREATED.getMessage(), supplyMessage);
    }

    public void sendSupplyDeleteMessage(String supplyId) {
        customerEventRM.sentMessage(DELETE_SUPPLY_TOPIC, EventType.DELETED.getMessage(), supplyId);
    }
}

