package com.github.pablohdzvizcarra;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class JsonFileSerializer<T> {
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

    public List<User> deserialize(Path filepath) {
        try {
            TypeReference<List<User>> typeRef = new TypeReference<List<User>>() {
            };
            return objectMapper.readValue(filepath.toFile(), typeRef);
        } catch (IOException e) {
            throw new IllegalStateException("Error trying to deserialize the file", e);
        }
    }

    public <U> List<U> deserialize(Path filepath, Class<U> elementClass) {
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(ArrayList.class, elementClass);

        try {
            return objectMapper.readValue(filepath.toFile(), listType);
        } catch (IOException e) {
            throw new IllegalStateException("Error trying to deserialize the file", e);
        }
    }

    public String readJson(Path filepath) {
        try {
            return objectMapper.readValue(filepath.toFile(), String.class);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "An error ocurred trying to deserialize the document: " + filepath.getFileName(), e);
        }
    }
}
