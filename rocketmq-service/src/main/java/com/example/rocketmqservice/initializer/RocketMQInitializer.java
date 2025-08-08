package com.example.rocketmqservice.initializer;

import com.example.rocketmqservice.model.TopicConfig;
import com.example.rocketmqservice.service.TopicConfigService;
import com.example.rocketmqservice.service.TopicOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RocketMQInitializer implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQInitializer.class);

    private final TopicConfigService topicConfigService;
    private final TopicOperationsService topicOperationsService;

    public RocketMQInitializer(TopicConfigService topicConfigService,
                               TopicOperationsService topicOperationsService) {
        this.topicConfigService = topicConfigService;
        this.topicOperationsService = topicOperationsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        topicConfigService.initializeConfigFile();
        refreshTopics();
    }

    public void refreshTopics() {
        try {
            List<TopicConfig> topicsToCreate = topicConfigService.loadTopicConfigurations();
            topicOperationsService.refreshTopics(topicsToCreate);
        } catch (Exception e) {
            LOGGER.error("Error refreshing topics: {}", e.getMessage(), e);
        }
    }

}
