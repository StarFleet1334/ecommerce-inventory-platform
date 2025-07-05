package com.example.rocketmqservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicConfig {
    private String name;
    private int readQueueNum;
    private int writeQueueNum;
}
