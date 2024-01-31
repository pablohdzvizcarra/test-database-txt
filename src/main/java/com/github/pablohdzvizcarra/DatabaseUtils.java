package com.github.pablohdzvizcarra;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseUtils {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getDatabaseNameFormatted(String databaseName) {
        if (databaseName == null)
            throw new NullPointerException("Database name is null");
        return databaseName + ".txt";
    }

    public String createDocumentId() {
        return UUID.randomUUID().toString().replace("-", "_");
    }

    /**
     * Adds a new id value to the json string.
     *
     * @param data the json string, the data needs to be a valid json
     * @param id   the id value
     * @return the json string with the id value
     */
    public String addIdValueToJson(String data, String id) {
        try {
            Map<String, Object> dataMap = objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
            dataMap.put("_id", id);

            return objectMapper.writeValueAsString(dataMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
