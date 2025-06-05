package com.example.inventoryservice.utils;

import lombok.Getter;

@Getter
public enum EventType {

    CREATED("CREATED"), DELETED("DELETED");

    private final String message;

    EventType(String message) {
        this.message = message;
    }

}
