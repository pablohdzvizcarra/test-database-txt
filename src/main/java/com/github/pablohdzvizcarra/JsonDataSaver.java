package com.github.pablohdzvizcarra;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDataSaver {
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves the given record inside a file with the specified filename.
     * The record must be a valid JSON string.
     *
     * @param jsonString the record to be saved
     * @param filepath   the filepath for the file to save the record
     * @return true if the record was successfully saved, false otherwise
     */
    public boolean save(String jsonString, String filepath) {
        validateJsonFormat(jsonString);
        validateFilepath(filepath);
        List<User> users = new ArrayList<>();
        User user = deserializeJsonToObject(jsonString); 
        users.add(user);

        try {
            File file = Paths.get(filepath).toFile();
            objectMapper.writeValue(file, users);
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }

    private User deserializeJsonToObject(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void validateFilepath(String filepath) {
        Objects.requireNonNull(filepath, "The filepath provided is null");
        if (Files.notExists(Paths.get(filepath))) {
            throw new IllegalStateException("The filepath provided does not exists");
        }
    }

    private void validateJsonFormat(String jsonString) {
        Objects.requireNonNull(jsonString, "The record provided is null");
        try {
            objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("The record provided is not a valid JSON", e);
        }
    }
}
