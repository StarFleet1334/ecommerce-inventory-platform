package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.messages.CustomerMessage;
import com.example.inventoryservice.entity.messages.CustomerOrderMessage;
import com.example.inventoryservice.event.publisher.CustomerEventPB;
import com.example.inventoryservice.event.publisher.CustomerOrderEventPB;
import com.example.inventoryservice.event.remover.CustomerEventRM;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerEventPB customerEventPB;
    private final CustomerEventRM customerEventRM;
    private final CustomerOrderEventPB customerOrderEventPB;

    @Value("${rocketmq.customer_add_topic}")
    private String ADD_CUSTOMER_TOPIC;

    @Value("${rocketmq.customer_delete_topic}")
    private String DELETE_CUSTOMER_TOPIC;

    @Value("${rocketmq.customer_order_topic}")
    private String ORDER_TOPIC;

    public void sendCustomerCreateMessage(CustomerMessage customer) {
        customerEventPB.sentMessage(ADD_CUSTOMER_TOPIC, EventType.CREATED.getMessage(), customer);
    }

    public void sendCustomerInitialCreateMessage(CustomerMessage customer) {
        customerEventPB.sentMessage(ADD_CUSTOMER_TOPIC, "INIT", customer);
    }

    public void sendCustomerDeleteMessage(String customerId) {
        customerEventRM.sentMessage(DELETE_CUSTOMER_TOPIC, EventType.DELETED.getMessage(), customerId);
    }

    public void sentCustomerOrderMessage(CustomerOrderMessage customerOrderMessage) {
        customerOrderMessage.setOrder_time(OffsetDateTime.now());
        customerOrderEventPB.sentMessage(ORDER_TOPIC, EventType.CREATED.getMessage(), customerOrderMessage);
    }

}
