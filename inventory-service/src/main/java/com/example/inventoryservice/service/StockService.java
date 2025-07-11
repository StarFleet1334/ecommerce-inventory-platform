package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.messages.StockMessage;
import com.example.inventoryservice.event.publisher.StockEventPB;
import com.example.inventoryservice.event.remover.StockEventRM;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockEventPB stockEventPB;
    private final StockEventRM stockEventRM;

    @Value("${rocketmq.stock_add_topic}")
    private String ADD_STOCK_TOPIC;

    @Value("${rocketmq.stock_delete_topic}")
    private String DELETE_STOCK_TOPIC;

    public void sendStockCreateMessage(StockMessage stock) {
        stockEventPB.sentMessage(ADD_STOCK_TOPIC, EventType.CREATED.getMessage(), stock);
    }

    public void sendStockInitialCreateMessage(StockMessage stock) {
        stockEventPB.sentMessage(ADD_STOCK_TOPIC, "INIT", stock);
    }

    public void sendStockDeleteMessage(String stockId) {
        stockEventRM.sentMessage(DELETE_STOCK_TOPIC, EventType.DELETED.getMessage(), stockId);
    }
}
