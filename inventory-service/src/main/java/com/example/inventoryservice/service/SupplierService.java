package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.messages.SupplierMessage;
import com.example.inventoryservice.event.publisher.SupplierEventPB;
import com.example.inventoryservice.event.remover.SupplierEventRM;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierEventPB supplierEventPB;
    private final SupplierEventRM supplierEventRM;

    @Value("${rocketmq.supplier_add_topic}")
    private String ADD_SUPPLIER_TOPIC;

    @Value("${rocketmq.supplier_delete_topic}")
    private String DELETE_SUPPLIER_TOPIC;

    public void sendSupplierCreateMessage(SupplierMessage supplier) {
        supplierEventPB.sentMessage(ADD_SUPPLIER_TOPIC, EventType.CREATED.getMessage(), supplier);
    }

    public void sendSupplierInitialCreateMessage(SupplierMessage supplier) {
        supplierEventPB.sentMessage(ADD_SUPPLIER_TOPIC, "INIT", supplier);
    }

    public void sendSupplierDeleteMessage(String supplierId) {
        supplierEventRM.sentMessage(DELETE_SUPPLIER_TOPIC, EventType.DELETED.getMessage(), supplierId);
    }

}
