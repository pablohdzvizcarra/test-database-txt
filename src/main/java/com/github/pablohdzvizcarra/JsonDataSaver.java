package com.github.pablohdzvizcarra;

import java.nio.file.Path;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The JsonDataSaver class is responsible for the communication between two
 * classes. This class works as a Adapter.
 * It provides methods to validate the JSON format, validate the filepath,
 * and manage the communication with a low level class.
 */
public class JsonDataSaver {
    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonFileSerializer jsonFileSerializer;
    private DatabaseUtils databaseUtils;

    public JsonDataSaver() {
        jsonFileSerializer = new JsonFileSerializer();
        databaseUtils = new DatabaseUtils();
    }

    private User deserializeJsonToObject(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void validateFilepath(Path filepath) {
        Objects.requireNonNull(filepath, "The filepath provided is null");
    }

    private void validateJsonFormat(String jsonString) {
        Objects.requireNonNull(jsonString, "The record provided is null");
        try {
            objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("The record provided is not a valid JSON", e);
        }
    }

    public void createDocumentInCollection(String data, Path filepath, String documentId) {
        validateFilepath(filepath);
        validateJsonFormat(data);
        String documentWithId = databaseUtils.addIdValueToJson(data, documentId);
        jsonFileSerializer.save(documentWithId, filepath);
    }

    public String readDocumentFromCollection(Path filepath) {
        validateFilepath(filepath);
        return jsonFileSerializer.deserializeFileIntoJson(filepath);
    }

    public void deleteDocumentFromCollection(Path filepath) {
        jsonFileSerializer.deleteJsonFile(filepath);
    }
}
