package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.messages.ProductMessage;
import com.example.inventoryservice.event.publisher.ProductEventPB;
import com.example.inventoryservice.event.remover.ProductEventRM;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductEventPB productEventPB;
    private final ProductEventRM productEventRM;

    @Value("${rocketmq.product_add_topic}")
    private String ADD_PRODUCT_TOPIC;

    @Value("${rocketmq.product_delete_topic}")
    private String DELETE_PRODUCT_TOPIC;

    public void sendProductCreateMessage(ProductMessage product) {
        productEventPB.sentMessage(ADD_PRODUCT_TOPIC, EventType.CREATED.getMessage(), product);
    }

    public void sendProductInitialCreateMessage(ProductMessage product) {
        productEventPB.sentMessage(ADD_PRODUCT_TOPIC, "INIT", product);
    }

    public void sendProductDeleteMessage(String productId) {
        productEventRM.sentMessage(DELETE_PRODUCT_TOPIC, EventType.DELETED.getMessage(), productId);
    }
}
