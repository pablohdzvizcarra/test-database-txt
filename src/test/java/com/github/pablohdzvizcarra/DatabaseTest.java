package com.github.pablohdzvizcarra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {
    private String databaseName = "example";
    private String rootDatabaseName = "database";
    private String databaseNameFormatted = "example.txt";
    private Database<User> databaseTxt;

    @BeforeEach
    void setUp() {
        databaseTxt = new Database(databaseName);
    }

    @Test
    void shouldCreateDirectoryWithTheDatabaseAsName() {
        boolean directoryExists = Files.exists(Paths.get(rootDatabaseName));
        assertTrue(directoryExists);
    }

    @Test
    void shouldCreateDirectoryWithTheDatabaseAsNameInDatabaseFolder() {
        boolean directoryExists = Files.exists(Paths.get("database", databaseNameFormatted));
        assertTrue(directoryExists);
    }

    @Test
    void shouldCreateNewFileWithNewDatabaseCreation() {
        databaseTxt.createNewDatabase("users");
        boolean fileExists = Files.exists(Paths.get("database", "users.txt"));

        assertTrue(fileExists);
    }

    @Test
    void shouldReturnTrueWhenRecordIsCreated() {
        User user = new User("John", "Conner", "john@gmail.com", "john123");
        boolean recordCreated = databaseTxt.createRecord(databaseName, user);
        assertTrue(recordCreated);
    }
}
