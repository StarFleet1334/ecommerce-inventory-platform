package com.example.orderprocessingservice.config.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.List;

public abstract class AbstractDataLoader<T, M> {
    protected final ObjectMapper objectMapper;
    protected final RestTemplate restTemplate;
    protected final String resourcePath;
    protected final String endpoint;
    protected final JpaRepository<T, ?> repository;

    protected AbstractDataLoader(ObjectMapper objectMapper,
                                 String resourcePath,
                                 String endpoint,
                                 JpaRepository<T, ?> repository) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
        this.resourcePath = resourcePath;
        this.endpoint = endpoint;
        this.repository = repository;
    }

    public void loadData() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        List<T> entities = objectMapper.readValue(is, getTypeReference());

        preProcessEntities(entities);

        for (T entity : entities) {
            M message = convertToMessage(entity);
            sendMessageToEndpoint(message, entity);
        }

        saveToRepository(entities);


        postProcessEntities(entities);
    }

    protected abstract TypeReference<List<T>> getTypeReference();
    protected abstract M convertToMessage(T entity);

    protected void preProcessEntities(List<T> entities) throws Exception {
    }

    protected void postProcessEntities(List<T> entities) throws Exception {
    }

    protected void saveToRepository(List<T> entities) {
        repository.saveAll(entities);
    }

    protected void sendMessageToEndpoint(M message, T entity) {
        String urlWithParam = endpoint + "?initialLoad=true";

        ResponseEntity<String> response = restTemplate.postForEntity(
                urlWithParam,
                message,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create entity: " + getEntityIdentifier(entity));
        }
    }

    protected abstract String getEntityIdentifier(T entity);
}
