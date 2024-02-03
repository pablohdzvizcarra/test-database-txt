package com.github.pablohdzvizcarra.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Low-level class provides functionality to serialize and deserialize JSON
 * strings into files.
 */
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

    public void deleteJsonFile(Path filepath) {
        try {
            Files.delete(filepath);
        } catch (IOException e) {
            throw new JsonFileSerializerException(
                    "An error ocurred trying to delete the document: " + filepath.getFileName(), e);
        }
    }

    public void updateJsonFile(String document, Path filepath) {
        deleteJsonFile(filepath);
        save(document, filepath);
    }

    /**
     * Deserializes the files in a folder into a JSON string.
     *
     * @param collectionPath the path of the folder containing the files
     * @return the JSON string representing the deserialized data
     * @throws JsonFileSerializerException if an error occurs during deserialization
     */
    public String deserializeFilesInFolderIntoJson(Path collectionPath) {
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        try (Stream<Path> files = Files.walk(collectionPath)) {
                List<HashMap<String, Object>> listData = files
                    .filter(Files::isRegularFile)
                    .map(filepath -> {
                        try {
                            return objectMapper.readValue(filepath.toFile(), String.class);
                        } catch (IOException e) {
                            throw new IllegalStateException("Error trying to deserialize the jsonString into Java Object " + e.getMessage(), e);
                        }
                    })
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                            });
                        } catch (IOException e) {
                            throw new IllegalStateException("Error trying to deserialize the jsonString into Java Object " + e.getMessage(), e);
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
            return objectMapper.writeValueAsString(listData);
        } catch (IOException e) {
            throw new JsonFileSerializerException(
                    "An error ocurred trying to deserialize the documents in the folder: " + collectionPath.getFileName(), e);
        }
    }
}
