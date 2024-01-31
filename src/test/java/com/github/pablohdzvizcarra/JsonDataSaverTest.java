package com.github.pablohdzvizcarra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonDataSaverTest {
    private final DatabaseUtils databaseUtils = new DatabaseUtils();
    private static final String ROOT_FOLDER = "database";
    private static final String COLLECTION_TEST = "test_database";
    private JsonDataSaver jsonDataSaver;

    @BeforeAll
    static void setUpAll() {
        if (Files.notExists(Paths.get(ROOT_FOLDER))) {
            try {
                Files.createDirectory(Paths.get(ROOT_FOLDER));
            } catch (Exception ignored) {
            }
        }

        if (Files.notExists(Paths.get(ROOT_FOLDER, COLLECTION_TEST))) {
            try {
                Files.createDirectory(Paths.get(ROOT_FOLDER, COLLECTION_TEST));
            } catch (Exception ignored) {
            }
        }
    }

    @BeforeEach
    void setUp() {
        jsonDataSaver = new JsonDataSaver();
    }

    @Test
    void shouldThrownExceptionWhenFilepathIsNull() {
        // Arrange
        String data = "";

        // Act
        assertThrows(NullPointerException.class,
                () -> jsonDataSaver.createDocumentInCollection(data, null),
                "The filepath provided is null");
    }

    @Test
    void shouldThrowExceptionWhenDataIsNull() {
        // Arrange
        Path filepath = Paths.get(ROOT_FOLDER, COLLECTION_TEST, "example_id");

        // Act
        assertThrows(NullPointerException.class,
                () -> jsonDataSaver.createDocumentInCollection(null, filepath),
                "The filepath provided is not valid");
    }

    @Test
    void shouldThrownStateExceptionWhenDataIsNotValidJson() {
        // Arrange
        Path filepath = Paths.get(ROOT_FOLDER, COLLECTION_TEST, "example_id");
        String data = "This is not a valid JSON";

        // Act
        assertThrows(IllegalStateException.class,
                () -> jsonDataSaver.createDocumentInCollection(data, filepath),
                "The record provided is not a valid JSON");
    }

    @Test
    void shouldReadDocumentFromCollectionWhenDocumentExists() {
        // Arrange
        String documentId = databaseUtils.createDocumentId();
        Path filepath = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentId);
        String data = """
                {
                    "name": "James",
                    "lastName": "Gosling"
                }
                """;

        // Act
        jsonDataSaver.createDocumentInCollection(data, filepath);
        String result = jsonDataSaver.readDocumentFromCollection(filepath);

        // Assert
        assertThat(result)
                .isNotNull()
                .contains("James")
                .contains("Gosling");
    }

    @Test
    void shouldAddIDToDocumentWhenDocumentIsCreated() {
        // Arrange
        String documentId = databaseUtils.createDocumentId();
        Path filepath = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentId);
        String data = """
                {
                    "name": "James",
                    "lastName": "Gosling"
                }
                """;

        // Act
        jsonDataSaver.createDocumentInCollection(data, filepath, documentId);
        String result = jsonDataSaver.readDocumentFromCollection(filepath);

        // Assert
        assertThat(result)
                .isNotNull()
                .contains(documentId);
    }
}
