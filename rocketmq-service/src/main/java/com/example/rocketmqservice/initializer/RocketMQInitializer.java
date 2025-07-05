package com.example.rocketmqservice.initializer;

import com.example.rocketmqservice.model.TopicConfig;
import com.example.rocketmqservice.service.TopicConfigService;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RocketMQInitializer implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQInitializer.class);

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.broker-address}")
    private String brokerAddress;

    private final TopicConfigService topicConfigService;

    public RocketMQInitializer(TopicConfigService topicConfigService) {
        this.topicConfigService = topicConfigService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        topicConfigService.initializeConfigFile();
        refreshTopics();
    }


    public void refreshTopics() {
        try {
            DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt("admin-group");
            mqAdminExt.setNamesrvAddr(nameServer);
            mqAdminExt.start();

            List<TopicConfig> topicsToCreate = topicConfigService.loadTopicConfigurations();

            for (TopicConfig config : topicsToCreate) {
                createTopic(mqAdminExt, config, brokerAddress);
            }

            mqAdminExt.shutdown();
        } catch (Exception e) {
            LOGGER.error("Error refreshing topics: {}", e.getMessage(), e);
        }
    }

    private void createTopic(DefaultMQAdminExt mqAdminExt, TopicConfig config, String brokerAddr) {
        try {
            org.apache.rocketmq.common.TopicConfig topicConfig = new org.apache.rocketmq.common.TopicConfig();
            topicConfig.setTopicName(config.getName());
            topicConfig.setReadQueueNums(config.getReadQueueNum());
            topicConfig.setWriteQueueNums(config.getWriteQueueNum());

            mqAdminExt.createAndUpdateTopicConfig(brokerAddr, topicConfig);
            LOGGER.info("Created/Updated topic: {}", config.getName());
        } catch (Exception e) {
            LOGGER.error("Error creating/updating topic {}: {}", config.getName(), e.getMessage());
        }
    }
}