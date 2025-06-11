package com.example.rocketmqservice.initializer;

import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class RocketMQInitializer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQInitializer.class);

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.broker-address}")
    private String brokerAddress;


    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt("admin-group");
        mqAdminExt.setNamesrvAddr(nameServer);
        mqAdminExt.start();

        List<com.example.rocketmqservice.model.TopicConfig> topicsToCreate = getTopicConfigurations();

        for (com.example.rocketmqservice.model.TopicConfig config : topicsToCreate) {
            createTopic(mqAdminExt, config, brokerAddress);
        }

        mqAdminExt.shutdown();
    }

    private List<com.example.rocketmqservice.model.TopicConfig> getTopicConfigurations() {
        return Arrays.asList(
                new com.example.rocketmqservice.model.TopicConfig("inventory_add", 4, 4),
                new com.example.rocketmqservice.model.TopicConfig("inventory_delete", 4, 4)
        );

    }

    private void createTopic(DefaultMQAdminExt mqAdminExt,
                             com.example.rocketmqservice.model.TopicConfig config,
                             String brokerAddr) {
        try {
            org.apache.rocketmq.common.TopicConfig topicConfig = new org.apache.rocketmq.common.TopicConfig();
            topicConfig.setTopicName(config.getName());
            topicConfig.setReadQueueNums(config.getReadQueueNum());
            topicConfig.setWriteQueueNums(config.getWriteQueueNum());

            mqAdminExt.createAndUpdateTopicConfig(brokerAddr, topicConfig);
            LOGGER.info("Created topic: {}", config.getName());
        } catch (Exception e) {
            LOGGER.error("Error creating topic {}: {}", config.getName(), e.getMessage());
        }
    }



}
