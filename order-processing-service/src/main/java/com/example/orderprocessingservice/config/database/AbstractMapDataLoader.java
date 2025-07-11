package com.example.orderprocessingservice.config.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapDataLoader<T, M> extends AbstractDataLoader<T, M> {
    protected AbstractMapDataLoader(ObjectMapper objectMapper,
                                    String resourcePath,
                                    String endpoint,
                                    JpaRepository<T, ?> repository) {
        super(objectMapper, resourcePath, endpoint, repository);
    }

    @Override
    public void loadData() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        List<Map<String, Object>> entries = objectMapper.readValue(is, getMapTypeReference());

        List<T> entities = processEntries(entries);

        if (!entities.isEmpty()) {

            for (T entity : entities) {
                M message = convertToMessage(entity);
                sendMessageToEndpoint(message, entity);
            }

            saveToRepository(entities);

            postProcessEntities(entities);
        }
    }

    protected abstract TypeReference<List<Map<String, Object>>> getMapTypeReference();
    protected abstract List<T> processEntries(List<Map<String, Object>> entries);
}
