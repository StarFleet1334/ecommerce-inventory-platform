package com.example.rocketmqservice.service;

import com.example.rocketmqservice.model.TopicConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TopicConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicConfigService.class);

    @Value("${app.config.path:config}")
    private String configDirPath;

    private final ObjectMapper objectMapper;

    public TopicConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<TopicConfig> loadTopicConfigurations() throws Exception {
        Path configPath = Paths.get(configDirPath, "topics.json");

        LOGGER.info("Loading config file from: {}", configPath.toAbsolutePath());

        if (!Files.exists(configPath)) {
            LOGGER.error("Config file not found at: {}", configPath.toAbsolutePath());
            throw new RuntimeException("Topics configuration file not found: " + configPath);
        }

        String content = Files.readString(configPath);
        return objectMapper.readValue(content, new TypeReference<List<TopicConfig>>() {});
    }

    public void initializeConfigFile() throws IOException {
        Path configDir = Paths.get(configDirPath);
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }

        Path configPath = configDir.resolve("topics.json");
        if (!Files.exists(configPath)) {
            org.springframework.core.io.Resource defaultConfig =
                    new org.springframework.core.io.ClassPathResource("config/topics.json");
            Files.copy(defaultConfig.getInputStream(), configPath);
            LOGGER.info("Created initial config file at: {}", configPath);
        }
    }

    public Path getConfigPath() {
        return Paths.get(configDirPath, "topics.json");
    }
}
