package com.example.inventoryservice.event.remover;

import com.example.inventoryservice.event.base.BaseEventRM;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Service;

@Service
public class SupplyEventRM extends BaseEventRM {
    public SupplyEventRM(DefaultMQProducer producer) {
        super(producer);
    }
}
