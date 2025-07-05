package com.example.rocketmqservice.watcher;


import com.example.rocketmqservice.initializer.RocketMQInitializer;
import com.example.rocketmqservice.service.TopicConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TopicConfigFileWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicConfigFileWatcher.class);

    private final RocketMQInitializer rocketMQInitializer;
    private final TopicConfigService topicConfigService;
    private final ExecutorService executorService;
    private WatchService watchService;
    private volatile boolean running = true;

    public TopicConfigFileWatcher(RocketMQInitializer rocketMQInitializer,
                                  TopicConfigService topicConfigService) {
        this.rocketMQInitializer = rocketMQInitializer;
        this.topicConfigService = topicConfigService;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @PostConstruct
    public void startWatching() {
        try {
            Path configPath = topicConfigService.getConfigPath();
            Path configDir = configPath.getParent();

            watchService = FileSystems.getDefault().newWatchService();
            configDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            executorService.submit(this::watchConfigFile);
            LOGGER.info("Started file watcher for {}", configPath);

        } catch (IOException e) {
            LOGGER.error("Error starting file watcher: {}", e.getMessage());
        }
    }

    private void watchConfigFile() {
        Path configPath = topicConfigService.getConfigPath();
        while (running) {
            try {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path changed = (Path) event.context();
                        if (changed.toString().equals(configPath.getFileName().toString())) {
                            LOGGER.info("Detected changes in topic configuration file");
                            rocketMQInitializer.refreshTopics();
                        }
                    }
                }
                key.reset();
            } catch (InterruptedException e) {
                LOGGER.error("File watching interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    @PreDestroy
    public void stopWatching() {
        running = false;
        executorService.shutdown();
        try {
            watchService.close();
        } catch (IOException e) {
            LOGGER.error("Error closing watch service: {}", e.getMessage());
        }
    }
}
