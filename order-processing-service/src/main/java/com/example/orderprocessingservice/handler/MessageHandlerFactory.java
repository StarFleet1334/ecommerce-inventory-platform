package com.example.orderprocessingservice.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageHandlerFactory {
    private final AddInventoryMessageHandler addInventoryMessageHandler;
    private final DeleteInventoryMessageHandler deleteInventoryMessageHandler;
    private Map<String, MessageHandler> handlers;

    @PostConstruct
    public void init() {
        handlers = new HashMap<>();
        handlers.put("inventory_add", addInventoryMessageHandler);
        handlers.put("inventory_delete", deleteInventoryMessageHandler);
    }

    public MessageHandler getHandler(String topic) {
        return handlers.get(topic);
    }
}
