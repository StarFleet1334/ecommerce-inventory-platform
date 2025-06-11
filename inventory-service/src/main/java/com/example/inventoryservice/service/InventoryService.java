package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.common.InventoryItem;
import com.example.inventoryservice.entity.response.InventoryItemResponse;
import com.example.inventoryservice.event.InventoryEventPublisher;
import com.example.inventoryservice.event.InventoryEventRemover;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryEventPublisher eventPublisher;
    private final InventoryEventRemover eventRemover;

    @Value("${rocketmq.inventory_add_topic}")
    private String TOPIC_INVENTORY;

    @Value("${rocketmq.inventory_delete_topic}")
    private String TOPIC_DELETE;

    public InventoryItemResponse sendInventoryCreatedMessage(InventoryItem item) {
        return eventPublisher.sentMessage(TOPIC_INVENTORY, EventType.CREATED.getMessage(), item);
    }

    public void sendInventoryDeletedMessage(String itemId) {
        eventRemover.sentMessage(TOPIC_DELETE, EventType.DELETED.getMessage(), itemId);
    }

}
