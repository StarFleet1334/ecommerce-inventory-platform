package com.example.inventoryservice.event.remover;

import com.example.inventoryservice.event.base.BaseEventRM;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Service;

@Service
public class CustomerEventRM extends BaseEventRM {

    public CustomerEventRM(DefaultMQProducer producer) {
        super(producer);
    }
}
