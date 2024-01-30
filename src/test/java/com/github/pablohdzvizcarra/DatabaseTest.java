package com.github.pablohdzvizcarra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {
    private String databaseName = "example";
    private String rootDatabaseName = "database";
    private Database<User> database;

    @BeforeEach
    void setUp() {
        database = new Database<>(databaseName);
    }

    @Test
    void shouldCreateRootDirectoryWhenTheDatabaseClassNewInstance() {
        boolean directoryExists = Files.exists(Paths.get(rootDatabaseName));
        assertTrue(directoryExists);
    }

    @Test
    void shouldCreateDirectoryEqualsToCollectionWhenCreateNewDatabase() {
        database.createNewDatabase("users");
        boolean directoryExists = Files.exists(Paths.get(rootDatabaseName, "users"));
        assertTrue(directoryExists);
    }

    @Test
    void shouldReturnTrueWhenRecordIsCreated() {
        User user = new User("John", "Conner", "john@gmail.com", "john123");
        boolean recordCreated = database.createDocument(databaseName, user);
        assertTrue(recordCreated);
    }
}
