package com.github.pablohdzvizcarra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {
    private final String databaseName = "test_database";
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
        String idDocument = database.createDocument(databaseName, user);
        assertThat(idDocument)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    void shouldReadDocumentWithValidIdAndCollection() {
        // Arrange
        User user = new User("James", "Gosling", "java@java.com", "java123");

        // Act
        String documentIdCreated = database.createDocument(databaseName, user);
        User document = database.readDocument(databaseName, documentIdCreated, User.class);

        // Assert
        assertThat(document)
                .isNotNull()
                .satisfies(validUser -> {
                    assertThat(validUser.getName()).isEqualTo(user.getName());
                    assertThat(validUser.getLastName()).isEqualTo(user.getLastName());
                    assertThat(validUser.getEmail()).isEqualTo(user.getEmail());
                    assertThat(validUser.getNickname()).isEqualTo(user.getNickname());
                    assertThat(validUser.getId()).isEqualTo(documentIdCreated);
                });
    }
}
