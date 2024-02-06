package com.github.pablohdzvizcarra.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a generic database that provides CRUD operations for storing and
 * retrieving data.
 *
 * @param <T> the type of the data to be stored in the database
 */
public class Database<T> {
    private DatabaseUtils databaseUtils;
    private static final String ROOT_FOLDER = "database";
    private final Logger logger = Logger.getLogger(Database.class.getName());
    private final JsonDataSaver jsonDataSaver = new JsonDataSaver();

    public Database(String collectionName) {
        databaseUtils = new DatabaseUtils();
        initRootDatabase();
        init(collectionName);
    }

    private void initRootDatabase() {
        if (Files.notExists(Paths.get(ROOT_FOLDER))) {
            try {
                Files.createDirectory(Paths.get(ROOT_FOLDER));
            } catch (IOException e) {
                throw new IllegalStateException("Error trying to create the root database", e);
            }
        }
    }

    public void createCollection(String collectionName) {
        init(collectionName);
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

    /**
     * Creates a new document in the specified collection with the given object.
     * 
     * @param collection the name of the collection where the document will be
     *                   created
     * @param object     the object to be stored as a document
     * @return the ID of the newly created document
     * @throws IllegalStateException if there is an error processing the object into
     *                               JSON format
     */
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

    public String createDocument(String collection, String document) {
        logger.log(Level.INFO, "Creating a new document in collection: {0}", collection);
        String documentId = databaseUtils.createDocumentId();
        Path filepath = createFilepath(collection, documentId);
        jsonDataSaver.createDocumentInCollection(document, filepath, documentId);
        return documentId;
    }

    private Path createFilepath(String collection, String documentId) {
        return Paths.get(ROOT_FOLDER, collection, documentId);
    }

    /**
     * Reads a document from the specified collection with the given document ID.
     *
     * @param <T>        the type of the document to be read
     * @param collection the name of the collection
     * @param documentId the ID of the document to be read
     * @param type       the class representing the type of the document
     * @return the document object of type T
     */
    public T readDocument(String collection, String documentId, Class<T> type) {
        validateCollection(collection);
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

    public String readDocument(String collection, String documentId) {
        logger.log(Level.INFO, "Reading document with id: {0}", documentId);
        validateCollection(collection);
        Path filepath = createFilepath(collection, documentId);
        return jsonDataSaver.readDocumentFromCollection(filepath);
    }

    /**
     * Validates if a collection exists in the database.
     *
     * @param collection the name of the collection to validate
     * @throws IllegalArgumentException if the collection does not exist
     */
    private void validateCollection(String collection) {
        Path path = Paths.get(ROOT_FOLDER, collection);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("The collection: " + collection + " does not exist");
        }
    }

    /**
     * Deletes a document from the specified collection.
     *
     * @param collection the name of the collection
     * @param documentId the ID of the document to be deleted
     */
    public void deleteDocument(String collection, String documentId) {
        logger.log(Level.INFO, "Deleting document with id: {0}", documentId);
        validateCollection(collection);
        Path filepath = createFilepath(collection, documentId);
        jsonDataSaver.deleteDocumentFromCollection(filepath);
    }

    /**
     * Updates a document in the specified collection with the given object.
     * The document Id returned is the same as the one provided.
     *
     * @param collection the name of the collection where the document is located
     * @param documentId the ID of the document to be updated
     * @param object     the object containing the updated data
     * @return the ID of the updated document needs to be the same as the one
     *         provided
     */
    public String updateDocument(String collection, String documentId, T object) {
        validateCollection(collection);
        Path filepath = createFilepath(collection, documentId);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String data = objectMapper.writeValueAsString(object);
            jsonDataSaver.updateDocumentInCollection(data, filepath, documentId);
            return documentId;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String updateDocument(String collection, String documentId, String document) {
        logger.log(Level.INFO, "Updating document with id: {0}", documentId);
        validateCollection(collection);
        Path filepath = createFilepath(collection, documentId);
        jsonDataSaver.updateDocumentInCollection(document, filepath, documentId);
        return documentId;
    }

    public String readAllDocumentsFromCollection(String collection) {
        logger.log(Level.INFO, "Reading all documents from collection: {0}", collection);
        validateCollection(collection);
        Path collectionPath = Paths.get(ROOT_FOLDER, collection);
        return jsonDataSaver.readAllDocumentsFromCollection(collectionPath);
    }
}
