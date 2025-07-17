package com.example.rocketmqservice.service;

import com.example.rocketmqservice.model.TopicConfig;
import org.apache.rocketmq.remoting.protocol.body.TopicList;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class TopicOperationsService {

    private static final Logger log = LoggerFactory.getLogger(TopicOperationsService.class);

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${rocketmq.broker-address}")
    private String brokerAddress;

    public void refreshTopics(List<TopicConfig> desired) {
        DefaultMQAdminExt admin = new DefaultMQAdminExt("admin-group");
        try {
            admin.setNamesrvAddr(nameServer);
            admin.start();

            TopicList topicList = admin.fetchAllTopicList();
            Set<String> existing = topicList.getTopicList();

            for (TopicConfig cfg : desired) {

                if (!existing.contains(cfg.getName())) {
                    createOrUpdate(admin, cfg);
                    continue;
                }

                org.apache.rocketmq.common.TopicConfig currentCfg =
                        admin.examineTopicConfig(brokerAddress, cfg.getName());

                boolean changed = currentCfg.getReadQueueNums()  != cfg.getReadQueueNum() ||
                        currentCfg.getWriteQueueNums() != cfg.getWriteQueueNum();

                if (changed) {
                    createOrUpdate(admin, cfg);
                } else {
                    log.debug("Topic '{}' unchanged; skipping", cfg.getName());
                }
            }
        } catch (Exception ex) {
            log.error("Topic refresh failed", ex);
        } finally {
            try { admin.shutdown(); } catch (Exception ignore) {}
        }
    }

    private void createOrUpdate(DefaultMQAdminExt admin, TopicConfig cfg) throws Exception {
        org.apache.rocketmq.common.TopicConfig tc = new org.apache.rocketmq.common.TopicConfig();
        tc.setTopicName(cfg.getName());
        tc.setReadQueueNums(cfg.getReadQueueNum());
        tc.setWriteQueueNums(cfg.getWriteQueueNum());

        admin.createAndUpdateTopicConfig(brokerAddress, tc);
        log.info("Created/Updated topic: {}", cfg.getName());
    }
}
