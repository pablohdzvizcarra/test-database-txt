package com.github.pablohdzvizcarra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Database<T> {
    private DatabaseUtils databaseUtils;
    private static final String ROOT_FOLDER = "database";
    private final Logger logger = Logger.getLogger(Database.class.getName());
    private final JsonDataSaver jsonDataSaver = new JsonDataSaver();

    public Database(String databaseName) {
        databaseUtils = new DatabaseUtils();
        init(databaseName);
    }

    /**
     * Initializes a new collection with the collection parameter as name.
     *
     * @param collection the name of the collection
     */
    private void init(String collection) {
        try {
            Files.createDirectory(Paths.get(ROOT_FOLDER, collection));
        } catch (IOException ignored) {
        }

    }

    /**
     * Creates a new database with the given name.
     *
     * @param databaseName the name of the new database
     */
    public void createNewDatabase(String databaseName) {
        init(databaseName);
    }

    public String createDocument(String collection, T object) {
        logger.log(Level.INFO, "Creating a new document in collection: {0}", collection);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String data = objectMapper.writeValueAsString(object);
            String documentId = databaseUtils.createDocumentId();
            Path filepath = createFilepath(collection, documentId);

            jsonDataSaver.createDocumentInCollection(data, filepath, documentId);
            return documentId;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private Path createFilepath(String collection, String documentId) {
        return Paths.get(ROOT_FOLDER, collection, documentId);
    }

    public T readDocument(String collection, String documentId, Class<T> type) {
        logger.log(Level.INFO, "Reading document with id: {0}", documentId);
        Path filepath = createFilepath(collection, documentId);
        String data = jsonDataSaver.readDocumentFromCollection(filepath);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, type);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
