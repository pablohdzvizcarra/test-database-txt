package com.github.pablohdzvizcarra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {
    private final String COLLECTION = "test_database";
    private String ROOT_DATABASE = "database";
    private Database<User> database;

    @BeforeEach
    void setUp() {
        database = new Database<>(COLLECTION);
    }

    @Test
    void shouldCreateRootDirectoryWhenTheDatabaseClassNewInstance() {
        boolean directoryExists = Files.exists(Paths.get(ROOT_DATABASE));
        assertTrue(directoryExists);
    }

    @Test
    void shouldCreateDirectoryEqualsToCollectionWhenCreateNewDatabase() {
        database.createNewDatabase("users");
        boolean directoryExists = Files.exists(Paths.get(ROOT_DATABASE, "users"));
        assertTrue(directoryExists);
    }

    @Test
    void shouldReturnTrueWhenRecordIsCreated() {
        User user = new User("John", "Conner", "john@gmail.com", "john123");
        String idDocument = database.createDocument(COLLECTION, user);
        assertThat(idDocument)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    void shouldReadDocumentWithValidIdAndCollection() {
        // Arrange
        User user = new User("James", "Gosling", "java@java.com", "java123");

        // Act
        String documentIdCreated = database.createDocument(COLLECTION, user);
        User document = database.readDocument(COLLECTION, documentIdCreated, User.class);

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

    @Test
    void shouldThrownExceptionWhenCollectionDoesNotExist() {
        // Arrange
        String collection = "wrong_collection";
        String documentId = "123";

        // Act
        Throwable throwable = catchThrowable(
                () -> database.readDocument(collection, documentId, User.class));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The collection: wrong_collection does not exist");
    }

    @Test
    void shouldDeleteAnExistingUserFromDatabase() {
        // Arrange
        User user = new User("James", "Gosling", "java@java.com", "java123");

        // Act
        String idDocumentCreated = database.createDocument(COLLECTION, user);

        // Assert
        assertThat(database.readDocument(COLLECTION, idDocumentCreated, User.class))
                .isNotNull()
                .satisfies(validUser -> {
                    assertThat(validUser.getName()).isEqualTo(user.getName());
                    assertThat(validUser.getLastName()).isEqualTo(user.getLastName());
                    assertThat(validUser.getEmail()).isEqualTo(user.getEmail());
                    assertThat(validUser.getNickname()).isEqualTo(user.getNickname());
                    assertThat(validUser.getId()).isEqualTo(idDocumentCreated);
                });

        // Act
        database.deleteDocument(COLLECTION, idDocumentCreated);
        Throwable throwable = catchThrowable(() -> database.readDocument(COLLECTION, idDocumentCreated, User.class));

        // Assert
        assertThat(throwable).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The id provided is not valid for any document in the collection");
    }

    @Test
    void shouldUpdateNameWhenDocumentExists() {
        // Arrange
        User user = new User("James", "Gosling", "james@java.com", "java_master");

        // Act
        String idDocumentCreated = database.createDocument(COLLECTION, user);
        assertThat(idDocumentCreated).isNotNull();

        var userToUpdate = new User("James", "Gosling", "james.gosling@java.com", "java_master");
        database.updateDocument(COLLECTION, idDocumentCreated, userToUpdate);
        User userUpdated = database.readDocument(COLLECTION, idDocumentCreated, User.class);

        // Assert
        assertThat(userUpdated)
                .isNotNull()
                .satisfies(validUser -> {
                    assertThat(validUser.getName()).isEqualTo(userToUpdate.getName());
                    assertThat(validUser.getLastName()).isEqualTo(userToUpdate.getLastName());
                    assertThat(validUser.getEmail()).isEqualTo(userToUpdate.getEmail());
                    assertThat(validUser.getNickname()).isEqualTo(userToUpdate.getNickname());
                    assertThat(validUser.getId()).isEqualTo(idDocumentCreated);
                });
    }

    @Test
    void shouldCreateDocumentWithAValidJsonString() {
        // Arrange
        String json = """
                {
                    "name": "James",
                    "lastName": "Gosling",
                    "email": "james@java.com",
                    "nickname": "java_master"
                }
                """;

        // Act
        String idDocumentCreated = database.createDocument(COLLECTION, json);

        // Assert
        assertThat(idDocumentCreated).isNotNull();

    }

    @Test
    void shouldReturJSONStringWhenDocumentIsRead() {
        // Arrange
        String json = """
                {
                    "name": "James",
                    "lastName": "Gosling",
                    "email": "james@java.com",
                    "nickname": "java_master"
                }
                """;

        // Act
        String idDocumentCreated = database.createDocument(COLLECTION, json);
        String document = database.readDocument(COLLECTION, idDocumentCreated);

        // Assert
        assertThat(document)
                .isNotNull()
                .contains("James")
                .contains("Gosling")
                .contains("james@java.com")
                .contains("java_master")
                .contains("_id")
                .contains(idDocumentCreated);
    }

    @Test
    void shouldReturnExceptionWhenTryReadNonExistsDocument() {
        // Arrange
        // Act
        Throwable throwable = catchThrowable(() -> database.readDocument(COLLECTION, "null"));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The id provided is not valid for any document in the collection");
    }
}
