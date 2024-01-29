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

    void save(String json, Path filepath) {
        try {
            T user = objectMapper.readValue(json, new TypeReference<T>() {
            });
            List<T> users = (List<T>) deserialize(filepath, User.class);
            users.add(user);
            objectMapper.writeValue(filepath.toFile(), users);
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
}
