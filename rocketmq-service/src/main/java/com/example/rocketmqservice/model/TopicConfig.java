package com.example.rocketmqservice.model;

public class TopicConfig {

    private final String name;
    private final int readQueueNum;
    private final int writeQueueNum;

    public TopicConfig(String name, int readQueueNum, int writeQueueNum) {
        this.name = name;
        this.readQueueNum = readQueueNum;
        this.writeQueueNum = writeQueueNum;
    }

    public String getName() {
        return name;
    }

    public int getReadQueueNum() {
        return readQueueNum;
    }

    public int getWriteQueueNum() {
        return writeQueueNum;
    }


}
