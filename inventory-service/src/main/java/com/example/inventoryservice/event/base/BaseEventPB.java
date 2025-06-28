package com.example.inventoryservice.event.base;

public interface BaseEventPB<T> {

    void sentMessage(String topic, String tag, T data);

}
