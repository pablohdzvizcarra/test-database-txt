package com.github.pablohdzvizcarra;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves the given JSON string into the specified collection.
     *
     * @param json     the JSON string to be saved
     * @param filepath the name of the collection to save the JSON.
     * @throws IllegalStateException if there is an error during serialization or
     *                               saving.
     */
    void save(String json, Path filepath) {
        try {
            objectMapper.writeValue(filepath.toFile(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error trying to deserialize the jsonString into Java Object", e);
        } catch (IOException e) {
            throw new IllegalStateException("Error trying to serialize the object into a file", e);
        }

    }

    /**
     * Deserializes the content of a file into a JSON string.
     *
     * @param filepath the path to the file to be deserialized
     * @return the deserialized JSON string
     * @throws IllegalStateException if an error occurs during deserialization
     */
    public String deserializeFileIntoJson(Path filepath) {
        try {
            return objectMapper.readValue(filepath.toFile(), String.class);
        } catch (IOException e) {
            throw new JsonFileSerializerException(
                    "An error ocurred trying to deserialize the document: " + filepath.getFileName(), e);
        }

    }
}
