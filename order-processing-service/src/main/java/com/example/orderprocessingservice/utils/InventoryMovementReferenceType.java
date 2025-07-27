package com.example.orderprocessingservice.utils;

import lombok.Getter;

@Getter
public enum InventoryMovementReferenceType {

    CUSTOMER_ORDER("customer_order",0),
    CUSTOMER_ORDER_DEBT_PAYED("customer_order_debt_payed",0),
    PURCHASE_ORDER("purchase_order",0),
    RECEIVE("Stock Received",1),
    DEBT_PAYED("Customer debt",12),
    SALE("Customer Sale",2);

    private final String value;
    private final int type_id;

    InventoryMovementReferenceType(String value,int type_id) {
        this.value = value;
        this.type_id = type_id;
    }

}
