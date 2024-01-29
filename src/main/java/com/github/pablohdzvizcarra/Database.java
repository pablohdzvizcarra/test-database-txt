package com.github.pablohdzvizcarra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Database<T> {
    private DatabaseUtils databaseUtils;
    private static final String ROOT_FOLDER = "database";

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

    public boolean createRecord(String database, T record) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String data = objectMapper.writeValueAsString(record);
            String databaseName = databaseUtils.getDatabaseNameFormatted(database);

            System.out.println(data);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        return true;
    }
}
