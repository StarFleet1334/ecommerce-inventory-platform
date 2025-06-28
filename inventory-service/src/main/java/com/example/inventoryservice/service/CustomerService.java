package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.messages.CustomerMessage;
import com.example.inventoryservice.event.publisher.CustomerEventPB;
import com.example.inventoryservice.event.remover.CustomerEventRM;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerEventPB customerEventPB;
    private final CustomerEventRM customerEventRM;

    @Value("${rocketmq.customer_add_topic}")
    private String ADD_CUSTOMER_TOPIC;

    @Value("${rocketmq.customer_delete_topic}")
    private String DELETE_CUSTOMER_TOPIC;

    public void sendCustomerCreateMessage(CustomerMessage customer) {
        customerEventPB.sentMessage(ADD_CUSTOMER_TOPIC, EventType.CREATED.getMessage(), customer);
    }

    public void sendCustomerDeleteMessage(String customerId) {
        customerEventRM.sentMessage(DELETE_CUSTOMER_TOPIC, EventType.DELETED.getMessage(), customerId);
    }
}
